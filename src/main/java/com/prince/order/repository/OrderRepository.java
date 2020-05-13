package com.prince.order.repository;


import com.prince.order.entity.Order;
import com.prince.order.enums.OrderStatus;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Order findByOrderId(String orderId);

    Order findByOrderIdAndDeliveryPersonMobileNo(String orderId, String mobileNo);

    Order findByStatusNotAndDeliveryPersonMobileNo(OrderStatus status, String mobileNo);
}
