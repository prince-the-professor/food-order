package com.prince.order.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prince.order.enums.OrderDeliveryStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderAssignResponse extends BaseResponse{
    private OrderDeliveryStatus status;
}
