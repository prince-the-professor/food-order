package com.prince.order.controller;

import com.prince.order.model.request.OrderAssignRequest;
import com.prince.order.model.response.AllAgentStatusResponse;
import com.prince.order.model.response.AgentStatusResponse;
import com.prince.order.model.response.OrderAssignResponse;
import com.prince.order.service.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    //use by admin to assign order
    @PostMapping("/order/assign")
    public ResponseEntity<OrderAssignResponse> assignOrder(@Valid @RequestBody OrderAssignRequest request) {
        return ResponseEntity.ok().body(adminService.assignOrder(request));
    }

    //for admin to fetch delivery agent status
    @GetMapping("/agent/status")
    public ResponseEntity<AgentStatusResponse> getStatus(@RequestParam String agentId){
        return ResponseEntity.ok().body(adminService.getPersonStatus(agentId));
    }

    //to get status of all delivery agent
    @GetMapping("/agent/status/all")
    public ResponseEntity<AllAgentStatusResponse> getStatus(){
        return ResponseEntity.ok().body(adminService.getAllAgentsStatus());
    }
}
