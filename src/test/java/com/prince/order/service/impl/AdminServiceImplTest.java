package com.prince.order.service.impl;

import com.prince.order.entity.DeliveryPerson;
import com.prince.order.entity.Order;
import com.prince.order.enums.OrderDeliveryStatus;
import com.prince.order.enums.OrderStatus;
import com.prince.order.model.request.OrderAssignRequest;
import com.prince.order.model.response.AgentStatusResponse;
import com.prince.order.model.response.OrderAssignResponse;
import com.prince.order.repository.DeliveryPersonRepository;
import com.prince.order.repository.OrderRepository;
import com.prince.order.service.IAdminService;
import com.prince.order.service.ICacheService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminServiceImplTest {
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private IAdminService adminService;
    private OrderRepository oRepo;
    private DeliveryPersonRepository dRepo;
    private ICacheService cacheService;

    @Test
    void assignOrderTest() {

        oRepo = mock(OrderRepository.class);
        dRepo = mock(DeliveryPersonRepository.class);
        cacheService = mock(ICacheService.class);
        adminService = new AdminServiceImpl(oRepo, dRepo, cacheService);
        OrderAssignRequest request = new OrderAssignRequest();
        request.setOrderId("123");
        request.setDeliveryPersonMobileNo("1234567890");

        DeliveryPerson dp = new DeliveryPerson();
        dp.setAvailable(true);

        Order order = new Order();
        order.setStatus(OrderStatus.ACCEPTED);

        when(dRepo.findByMobileNo(request.getDeliveryPersonMobileNo())).thenReturn(dp);
        when(cacheService.fetchOrder(isA(String.class))).thenReturn(order);
        when(oRepo.findByOrderId(request.getOrderId())).thenReturn(order);
        order.setDeliveryPerson(dp);
        when(oRepo.save(isA(Order.class))).thenReturn(order);
        when(dRepo.save(isA(DeliveryPerson.class))).thenReturn(dp);
        OrderAssignResponse orderAssignResponse = adminService.assignOrder(request);
        assertEquals(OrderDeliveryStatus.ACCEPTED, orderAssignResponse.getStatus());
    }


    @Test
    void getPersonStatus() {
        oRepo = mock(OrderRepository.class);
        dRepo = mock(DeliveryPersonRepository.class);
        cacheService = mock(ICacheService.class);
        adminService = new AdminServiceImpl(oRepo, dRepo, cacheService);
        Order order = new Order();
        order.setOrderId("123");
        order.setPreparationTime(20);
        order.setCreatedOn(new Date());

        when(oRepo.findByStatusNotAndDeliveryPersonMobileNo(OrderStatus.DELIVERED, "9170775458")).thenReturn(order);
        AgentStatusResponse response = adminService.getPersonStatus("9170775458");
        assertEquals("9170775458", response.getAgentId());
    }

    @Test
    void getAllAgentsStatus() {
    }
}