package com.prince.order.controller;

import com.prince.order.entity.Item;
import com.prince.order.model.request.AddNewAgentRequest;
import com.prince.order.model.response.BaseResponse;
import com.prince.order.service.IInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/inventory")
@Slf4j
public class InventoryController {

    private final IInventoryService inventoryService;

    public InventoryController(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    //use by inventory team
    @PostMapping("/item")
    public ResponseEntity<BaseResponse> addItem(@Valid @RequestBody Item item) {
        log.info("inventory addition request : " + item);
        return ResponseEntity.ok().body(inventoryService.addItem(item));
    }

    //used by admin to get all items
    @GetMapping("/item")
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok().body(inventoryService.getAllItems());
    }

    //use by inventory-admin team
    @PostMapping("/agent")
    public ResponseEntity<BaseResponse> addDeliveryPerson(@Valid @RequestBody AddNewAgentRequest request) {
        log.info("request to add delivery boy : " + request);
        return ResponseEntity.ok().body(inventoryService.addDeliveryPerson(request));
    }
}
