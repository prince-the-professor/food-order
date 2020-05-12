//package com.prince.order.service.impl;
//
//import com.prince.order.repository.DeliveryPersonRepository;
//import com.prince.order.repository.ItemRepository;
//import com.prince.order.repository.OrderItemRepository;
//import com.prince.order.repository.OrderRepository;
//import com.prince.order.service.ICacheService;
//import com.prince.order.service.IOrderService;
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.MockitoAnnotations;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import static org.mockito.Mockito.mock;
//
//@RunWith(MockitoJUnitRunner.class)
//class OrderServiceImplTest {
//
//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    private IOrderService orderService;
//    private OrderItemRepository orderItemRepository;
//    private OrderRepository orderRepository;
//    private ICacheService cacheService;
//    private ItemRepository itemRepository;
//    private DeliveryPersonRepository deliveryPersonRepository;
//
//    @Test
//    void orderPlace() {
//        orderRepository = mock(OrderRepository.class);
//        orderItemRepository = mock(OrderItemRepository.class);
//        cacheService = mock(ICacheService.class);
//        itemRepository = mock(ItemRepository.class);
//        deliveryPersonRepository = mock(DeliveryPersonRepository.class);
//        orderService = new OrderServiceImpl(itemRepository, orderItemRepository, orderRepository,
//                deliveryPersonRepository, cacheService);
//    }
//
//    @Test
//    void getOrderStatus() {
//    }
//
//    @Test
//    void updateStatus() {
//    }
//}