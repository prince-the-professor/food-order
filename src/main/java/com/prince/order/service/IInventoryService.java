package com.prince.order.service;

import com.prince.order.entity.Item;
import com.prince.order.entity.User;
import com.prince.order.model.request.AddNewAgentRequest;
import com.prince.order.model.request.AddUserRequest;
import com.prince.order.model.response.BaseResponse;

import java.util.List;

public interface IInventoryService {

    BaseResponse addItem(Item item);

    List<Item> getAllItems();

    BaseResponse addUser(AddUserRequest user);

    User ValidateUser(AddUserRequest request);

    BaseResponse addDeliveryPerson(AddNewAgentRequest request);
}
