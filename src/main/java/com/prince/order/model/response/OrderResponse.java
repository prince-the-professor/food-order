package com.prince.order.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse extends BaseResponse {
    private String orderId;
    private double billAmount;
    private double discount;
    private long deliverIn;
}
