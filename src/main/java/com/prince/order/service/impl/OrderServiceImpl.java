package com.prince.order.service.impl;

import com.fasterxml.uuid.Generators;
import com.prince.order.entity.DeliveryPerson;
import com.prince.order.entity.Item;
import com.prince.order.entity.Order;
import com.prince.order.entity.OrderItem;
import com.prince.order.enums.OrderStatus;
import com.prince.order.model.request.OrderRequest;
import com.prince.order.model.request.OrderStatusUpdateRequest;
import com.prince.order.model.response.OrderResponse;
import com.prince.order.model.response.OrderStatusUpdateResponse;
import com.prince.order.repository.DeliveryPersonRepository;
import com.prince.order.repository.ItemRepository;
import com.prince.order.repository.OrderItemRepository;
import com.prince.order.repository.OrderRepository;
import com.prince.order.service.ICacheService;
import com.prince.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final ICacheService cacheService;

    public OrderServiceImpl(ItemRepository itemRepository, OrderItemRepository orderItemRepository, OrderRepository orderRepository, DeliveryPersonRepository deliveryPersonRepository, ICacheService cacheService) {
        this.itemRepository = itemRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.cacheService = cacheService;
    }

    @Override
    @Transactional
    public OrderResponse orderPlace(OrderRequest orderRequest) {
        OrderResponse response = new OrderResponse();
        List<String> itemIds = new ArrayList<>(orderRequest.getItemQuantity().keySet());
        try {
            List<Item> itemList = itemRepository.findByItemIdIn(itemIds);
            if (itemIds.size() == itemList.size()) {
                createCartAndOrder(orderRequest, itemList, response);
                response.setMessage("order created");
            } else {
                response.setMessage("item missing");
            }
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage("error occurred while cart and order creation");
            log.error("error occurred while cart and order creation : " + e);
            throw new RuntimeException("error occurred while cart and order creation");
        }
        return response;
    }

    @Override
    public OrderStatusUpdateResponse getOrderStatus(String orderId) {
        OrderStatusUpdateResponse response = new OrderStatusUpdateResponse();
        Order order = cacheService.fetchOrder(orderId);
        if (order != null) {
            response.setSuccess(true);
            response.setStatus(order.getStatus());
        } else {
            response.setMessage("orderId does not exist");
            response.setSuccess(false);
            log.error("orderId does not exist ");
        }
        return response;
    }

    @Override
    public OrderStatusUpdateResponse updateStatus(OrderStatusUpdateRequest request) {
        OrderStatusUpdateResponse response = new OrderStatusUpdateResponse();
        Order order = orderRepository.findByOrderIdAndDeliveryPersonMobileNo(request.getOrderId(),
                request.getMobileNo());
        if (order != null) {
            updateOrder(request, response, order);
        } else {
            response.setSuccess(false);
            response.setMessage("Not updated");
        }
        return response;
    }

    private void updateOrder(OrderStatusUpdateRequest request, OrderStatusUpdateResponse response, Order order) {
        if (request.getStatus() == OrderStatus.DELIVERED) {
            markDeliveryPersonAvailable(request.getMobileNo());
        }
        order.setStatus(request.getStatus());
        cacheService.saveOrder(order);
        response.setSuccess(true);
        response.setMessage("status updated");
        response.setStatus(request.getStatus());
    }

    private void markDeliveryPersonAvailable(String mobileNo) {
        DeliveryPerson person = deliveryPersonRepository.findByMobileNo(mobileNo);
        person.setAvailable(true);
        person.setOrderId(null);
        deliveryPersonRepository.save(person);
    }

    private void createCartAndOrder(OrderRequest orderRequest, List<Item> itemList, OrderResponse response) {
        List<OrderItem> orderItemList = new ArrayList<>();
        String orderId = "fdo_" + Generators.timeBasedGenerator().generate().toString();
        double billAmount = 0, discount = 0, dc, price;
        long totalPreparationTime = 0;
        for (Item item : itemList) {
            long quantity = orderRequest.getItemQuantity().get(item.getItemId());
            dc = item.getDiscountValue() * quantity;
            price = item.getPrice() * quantity - dc;
            orderItemList.add(new OrderItem().setOrderId(orderId).setPrice(price).setItem(item).setQuantity(quantity));
            discount += dc;
            billAmount += price;

            totalPreparationTime += (item.getPreparationTime() * quantity);
        }
        orderItemRepository.saveAll(orderItemList);
        prepareOrder(orderRequest, orderId, billAmount, discount, totalPreparationTime, response);
    }

    private void prepareOrder(OrderRequest orderRequest, String orderId, double billAmount, double discount,
            long totalPreparationTime, OrderResponse response) {
        Order order =
                new Order.OrderBuilder()
                        .setOrderId(orderId)
                        .setOrderAmount(billAmount)
                        .setCustomerMobile(orderRequest.getMobileNo())
                        .setDiscount(discount)
                        .setDeliveryAddress(orderRequest.getDeliveryAddress().toString())
                        .setPreparationTime(totalPreparationTime)
                        .build();
        cacheService.saveOrder(order);

        response.setOrderId(orderId);
        response.setBillAmount(billAmount);
        response.setDiscount(discount);
        response.setDeliverIn(totalPreparationTime);
    }
}
