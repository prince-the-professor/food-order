package com.prince.order.service.impl;

import com.prince.order.entity.DeliveryPerson;
import com.prince.order.entity.Order;
import com.prince.order.enums.OrderDeliveryStatus;
import com.prince.order.enums.OrderStatus;
import com.prince.order.model.request.OrderAssignRequest;
import com.prince.order.model.response.AgentStatusResponse;
import com.prince.order.model.response.AllAgentStatusResponse;
import com.prince.order.model.response.OrderAssignResponse;
import com.prince.order.repository.DeliveryPersonRepository;
import com.prince.order.repository.OrderRepository;
import com.prince.order.service.IAdminService;
import com.prince.order.service.ICacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AdminServiceImpl implements IAdminService {

    private final OrderRepository orderRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final ICacheService cacheService;

    public AdminServiceImpl(OrderRepository orderRepository, DeliveryPersonRepository deliveryPersonRepository, ICacheService cacheService) {
        this.orderRepository = orderRepository;
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.cacheService = cacheService;
    }

    @Override
    public OrderAssignResponse assignOrder(OrderAssignRequest request) {
        OrderAssignResponse response = new OrderAssignResponse();
        DeliveryPerson person = deliveryPersonRepository.findByMobileNo(request.getDeliveryPersonMobileNo());
        Order order = cacheService.fetchOrder(request.getOrderId());
        if (person.isAvailable() && order.getStatus() == OrderStatus.ACCEPTED) {
            updateOrder(person, order);
            updateDeliveryAgent(request, person);
            response.setStatus(OrderDeliveryStatus.ACCEPTED);
            response.setSuccess(true);
            response.setMessage("order assigned");
        } else {
            response.setStatus(OrderDeliveryStatus.DECLINED);
            response.setSuccess(false);
            response.setMessage("person is not available or order is already assigned");
            log.error("person is not available or order is already assigned");
            throw new RuntimeException("person is not available or order is already assigned");
        }
        return response;
    }


    private void updateDeliveryAgent(OrderAssignRequest request, DeliveryPerson person) {
        person.setAvailable(false);
        person.setOrderId(request.getOrderId());
        deliveryPersonRepository.save(person);
    }

    private void updateOrder(DeliveryPerson person, Order order) {
        order.setDeliveryPerson(person);
        order.setStatus(OrderStatus.ASSIGNED);
        cacheService.saveOrder(order);
    }

    @Override
    public AgentStatusResponse getPersonStatus(String mobileNo) {
        AgentStatusResponse response = new AgentStatusResponse();
        Order order = orderRepository.findByStatusNotAndDeliveryPersonMobileNo(OrderStatus.DELIVERED, mobileNo);
        if (order != null) {
            response.setAssignedOrder(order.getOrderId());
            response.setAvailable(false);

            long timeLeft = calculateTimeLeft(order.getCreatedOn(), order.getPreparationTime());
            response.setTimeLeftToDeliver(String.valueOf(timeLeft));
            response.setMessage("active");
        } else {
            response.setAvailable(true);
            response.setMessage("not-active");
        }
        response.setAgentId(mobileNo);
        response.setSuccess(true);
        return response;
    }

    private long calculateTimeLeft(Date orderPlacedTime, long preparationTime) {
        Calendar expectedTime = Calendar.getInstance();
        expectedTime.setTime(orderPlacedTime);
        expectedTime.add(Calendar.MINUTE, Math.toIntExact(preparationTime));
        long timeLeft = expectedTime.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        return TimeUnit.MILLISECONDS.toMinutes(timeLeft);
    }

    @Override
    public AllAgentStatusResponse getAllAgentsStatus() {
        AllAgentStatusResponse response = new AllAgentStatusResponse();
        List<DeliveryPerson> personList = new ArrayList<>();
        deliveryPersonRepository.findAll().forEach(personList::add);
        List<AgentStatusResponse> agentStatusList = new ArrayList<>();
        personList.forEach(person -> agentStatusList.add(
                (AgentStatusResponse)new AgentStatusResponse().setAgentId(person.getMobileNo())
                        .setAvailable(person.isAvailable())
                        .setAssignedOrder(person.getOrderId())
                        .setSuccess(true)));
        response.setStatusList(agentStatusList);
        response.setSuccess(true);
        response.setMessage("success");
        return response;
    }
}
