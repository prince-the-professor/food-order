package com.prince.order.service;

import com.prince.order.model.request.OrderAssignRequest;
import com.prince.order.model.response.AllAgentStatusResponse;
import com.prince.order.model.response.AgentStatusResponse;
import com.prince.order.model.response.OrderAssignResponse;

public interface IAdminService {
    OrderAssignResponse assignOrder(OrderAssignRequest request);

    AgentStatusResponse getPersonStatus(String mobileNo);

    AllAgentStatusResponse getAllAgentsStatus();
}
