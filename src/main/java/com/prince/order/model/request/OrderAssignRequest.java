package com.prince.order.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class OrderAssignRequest {
    @NotNull
    @NotEmpty(message = "mobileNo is required")
    @Pattern(regexp = "^[1-9][0-9]{9}$")
    private String deliveryPersonMobileNo;
    @NotNull
    @NotEmpty(message = "orderId is required")
    private String orderId;
}
