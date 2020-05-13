package com.prince.order.service.impl;

import com.fasterxml.uuid.Generators;
import com.prince.order.entity.*;
import com.prince.order.enums.OrderStatus;
import com.prince.order.model.request.AddUserRequest;
import com.prince.order.model.request.OrderRequest;
import com.prince.order.model.request.OrderStatusUpdateRequest;
import com.prince.order.model.response.OrderResponse;
import com.prince.order.model.response.OrderStatusUpdateResponse;
import com.prince.order.repository.DeliveryPersonRepository;
import com.prince.order.repository.ItemRepository;
import com.prince.order.repository.OrderItemRepository;
import com.prince.order.repository.OrderRepository;
import com.prince.order.service.ICacheService;
import com.prince.order.service.IInventoryService;
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
    private final IInventoryService inventoryService;

    public OrderServiceImpl(ItemRepository itemRepository, OrderItemRepository orderItemRepository, OrderRepository orderRepository, DeliveryPersonRepository deliveryPersonRepository, ICacheService cacheService, IInventoryService inventoryService) {
        this.itemRepository = itemRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.cacheService = cacheService;
        this.inventoryService = inventoryService;
    }

    @Override
    @Transactional
    public OrderResponse orderPlace(OrderRequest orderRequest) {
        OrderResponse response = new OrderResponse();
        List<String> itemIds = new ArrayList<>(orderRequest.getItemQuantity().keySet());
        try {
            AddUserRequest request = new AddUserRequest();
            request.setMobileNo(orderRequest.getMobileNo());
            request.setAddress(orderRequest.getDeliveryAddress());
            User user = inventoryService.ValidateUser(request);
            List<Item> itemList = itemRepository.findByItemIdIn(itemIds);
            if (itemIds.size() == itemList.size()) {
                createCartAndOrder(orderRequest, itemList, response, user);
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

    private void createCartAndOrder(OrderRequest orderRequest, List<Item> itemList, OrderResponse response, User user) {
        List<OrderItem> cart = new ArrayList<>();
        String orderId = "fdo_" + Generators.timeBasedGenerator().generate().toString();
        double billAmount = 0, orderDiscount = 0, itemDiscount, actual_price, final_price;
        long totalPreparationTime = 0;
        for (Item item : itemList) {
            long quantity = orderRequest.getItemQuantity().get(item.getItemId());
            itemDiscount = item.getDiscountValue() * quantity;
            actual_price = item.getPrice() * quantity;
            final_price = actual_price - itemDiscount;
            cart.add(new OrderItem()
                    .setOrderId(orderId)
                    .setItem(item)
                    .setQuantity(quantity)
                    .setDiscount(itemDiscount)
                    .setActualPrice(actual_price)
                    .setFinalPrice(final_price));
            orderDiscount += itemDiscount;
            billAmount += final_price;

            totalPreparationTime += (item.getPreparationTime() * quantity);
        }
        orderItemRepository.saveAll(cart);
        prepareOrder(orderRequest, orderId, billAmount, orderDiscount, totalPreparationTime, user, response);
    }

    private void prepareOrder(OrderRequest orderRequest, String orderId, double billAmount, double discount,
            long totalPreparationTime, User user, OrderResponse response) {
        Order order =
                new Order.OrderBuilder()
                        .setOrderId(orderId)
                        .setOrderAmount(billAmount)
                        .setDiscount(discount)
                        .setDeliveryAddress(orderRequest.getDeliveryAddress() != null ?
                                orderRequest.getDeliveryAddress().toString() : user.getAddress())
                        .setUser(user)
                        .setPreparationTime(totalPreparationTime)
                        .build();
        cacheService.saveOrder(order);

        response.setOrderId(orderId);
        response.setBillAmount(billAmount);
        response.setDiscount(discount);
        response.setDeliverIn(totalPreparationTime);
    }
}
