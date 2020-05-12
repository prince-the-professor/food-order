package com.prince.order.controller;

import com.prince.order.model.request.OrderRequest;
import com.prince.order.model.request.OrderStatusUpdateRequest;
import com.prince.order.model.response.OrderResponse;
import com.prince.order.model.response.OrderStatusUpdateResponse;
import com.prince.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    private final IOrderService IOrderService;

    public OrderController(IOrderService IOrderService) {
        this.IOrderService = IOrderService;
    }

    //use by user to place order
    @PostMapping("/place")
    public ResponseEntity<OrderResponse> orderPlace(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok().body(IOrderService.orderPlace(request));
    }

    //use by delivery user or admin to fetch order status
    @GetMapping("/status")
    public ResponseEntity<OrderStatusUpdateResponse> getOrderStatus(@RequestParam String orderId) {
        return ResponseEntity.ok().body(IOrderService.getOrderStatus(orderId));
    }

    //use by delivery person to update order status
    @PutMapping("/status")
    public ResponseEntity<OrderStatusUpdateResponse> updateStatus(@Valid @RequestBody OrderStatusUpdateRequest request) {
        return ResponseEntity.ok().body(IOrderService.updateStatus(request));
    }

}
