package com.prince.order.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.prince.order.entity.Order;
import com.prince.order.repository.OrderRepository;
import com.prince.order.service.ICacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheServiceImpl implements ICacheService {

    private final OrderRepository orderRepository;
    private final Cache<String, Order> cache;

    public CacheServiceImpl(OrderRepository orderRepository, Cache<String, Order> cache) {
        this.orderRepository = orderRepository;
        this.cache = cache;
    }

    @Override
    public void saveOrder(Order order) {
        orderRepository.save(order);
        cache.put(order.getOrderId(), order);
    }

    @Override
    public Order fetchOrder(String orderId) {
        Order order = cache.getIfPresent(orderId);
        if (order == null) {
            log.info("orderId: {} is not present in cache...fetching from DB and saving again in cache", orderId);
            order = orderRepository.findByOrderId(orderId);
            cache.put(orderId, order);
        }
        return order;
    }
}
