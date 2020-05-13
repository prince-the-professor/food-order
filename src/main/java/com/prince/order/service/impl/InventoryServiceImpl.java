package com.prince.order.service.impl;

import com.prince.order.entity.DeliveryPerson;
import com.prince.order.entity.Item;
import com.prince.order.entity.User;
import com.prince.order.model.request.AddNewAgentRequest;
import com.prince.order.model.request.AddUserRequest;
import com.prince.order.model.response.BaseResponse;
import com.prince.order.repository.DeliveryPersonRepository;
import com.prince.order.repository.ItemRepository;
import com.prince.order.repository.UserRepository;
import com.prince.order.service.IInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InventoryServiceImpl implements IInventoryService {

    private final ItemRepository itemRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public InventoryServiceImpl(ItemRepository itemRepository, DeliveryPersonRepository deliveryPersonRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public BaseResponse addItem(Item item) {
        BaseResponse response = new BaseResponse();
        try {
            itemRepository.save(item);
            response.setSuccess(true);
            response.setMessage("item added");
        } catch (Exception e) {
            response.setMessage("item already exist or db issue");
            log.error("item already exist or db issue : " + e);
        }
        return response;
    }

    @Override
    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();
        itemRepository.findAll().forEach(itemList::add);
        return itemList;
    }

    @Override
    public BaseResponse addUser(AddUserRequest request) {
        BaseResponse response = new BaseResponse();
        try {
//            checkIfAlreadyExist(request);
            ValidateUser(request);
            response.setMessage("success");
            response.setSuccess(true);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("error");
            log.error("db error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public User ValidateUser(AddUserRequest request) {
        User user = new User();
        Optional<User> userOptional = userRepository.findById(request.getMobileNo());
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user.setMobileNo(request.getMobileNo());
        }
        if (!StringUtils.isEmpty(request.getUserName()))
            user.setUserName(request.getUserName());
        user.setAddress(request.getAddress().toString());
        user = userRepository.save(user);
        return user;
    }

    @Override
    public BaseResponse addDeliveryPerson(AddNewAgentRequest request) {
        BaseResponse response = new BaseResponse();
        try {
            DeliveryPerson person = modelMapper.map(request, DeliveryPerson.class);
            deliveryPersonRepository.save(person);
            response.setSuccess(true);
            response.setMessage("delivery person added");
        } catch (Exception e) {
            response.setMessage("mobile no already exist or db issue");
            log.error("mobile no already exist or db issue : " + e);
        }
        return response;
    }

}
