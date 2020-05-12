package com.prince.order.service.impl;

import com.prince.order.entity.DeliveryPerson;
import com.prince.order.entity.Item;
import com.prince.order.model.request.AddNewAgentRequest;
import com.prince.order.model.response.BaseResponse;
import com.prince.order.repository.DeliveryPersonRepository;
import com.prince.order.repository.ItemRepository;
import com.prince.order.service.IInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InventoryServiceImpl implements IInventoryService {

    private final ItemRepository itemRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final ModelMapper modelMapper;

    public InventoryServiceImpl(ItemRepository itemRepository, DeliveryPersonRepository deliveryPersonRepository, ModelMapper modelMapper) {
        this.itemRepository = itemRepository;
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.modelMapper = modelMapper;
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
