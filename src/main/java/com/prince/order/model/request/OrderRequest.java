package com.prince.order.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.HashMap;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequest implements Serializable {
    @NotNull
    @NotEmpty(message = "mobileNo is required")
    @Pattern(regexp = "^[1-9][0-9]{9}$")
    private String mobileNo;
    //map of itemId with noOfQuantity
    @NotEmpty
    private HashMap<String, Long> itemQuantity;

    @NotNull
    private Address deliveryAddress;
}
