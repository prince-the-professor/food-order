package com.prince.order.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentStatusResponse extends BaseResponse {
    private String agentId;
    private boolean available;
    private String assignedOrder;
    private String timeLeftToDeliver;
}
