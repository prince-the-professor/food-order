package com.prince.order.controller;

import com.prince.order.model.request.OrderAssignRequest;
import com.prince.order.model.response.AgentStatusResponse;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class ThreadController implements Runnable {
    @SneakyThrows
    @Override
    public void run() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        OrderAssignRequest request = new OrderAssignRequest();
        request.setOrderId("fdo_0adaea49-952c-11ea-b599-019096b37ea8");
        request.setDeliveryPersonMobileNo("9170775452");
        headers.add("userId", "9170775458");
        headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<OrderAssignRequest> entity = new HttpEntity<>(request, headers);

        Thread.sleep(5000);
        System.out.println(request);
        ResponseEntity<AgentStatusResponse> response = restTemplate.exchange("http://localhost:8081/admin/order/assign",
                HttpMethod.POST, entity, AgentStatusResponse.class);
        System.out.println(Thread.currentThread().getId() + " : " + response.getBody());
    }
}
