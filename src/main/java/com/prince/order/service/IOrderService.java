package com.prince.order.service;

import com.prince.order.model.request.OrderRequest;
import com.prince.order.model.request.OrderStatusUpdateRequest;
import com.prince.order.model.response.OrderResponse;
import com.prince.order.model.response.OrderStatusUpdateResponse;

public interface IOrderService {

    OrderResponse orderPlace(OrderRequest orderRequest);

    OrderStatusUpdateResponse getOrderStatus(String orderId);

    OrderStatusUpdateResponse updateStatus(OrderStatusUpdateRequest request);
}
