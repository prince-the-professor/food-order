package com.prince.order.service;

import com.prince.order.entity.Order;

public interface ICacheService {
    void saveOrder(Order order);

    Order fetchOrder(String orderId);
}
