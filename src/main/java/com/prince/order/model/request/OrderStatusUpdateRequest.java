package com.prince.order.model.request;

import com.prince.order.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Accessors(chain = true)
public class OrderStatusUpdateRequest {
    @NotNull
    @NotEmpty(message = "mobileNo is required")
    @Pattern(regexp = "^[1-9][0-9]{9}$")
    private String mobileNo;
    @NotNull
    @NotEmpty(message = "orderId is required")
    private String orderId;
    @NotNull
    private OrderStatus status;
}
