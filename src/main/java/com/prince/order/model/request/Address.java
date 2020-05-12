package com.prince.order.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    @NotEmpty(message = "house no is required")
    private String houseNo;
    private String landmark;
    private String areaOrLocality;
    private String city;
    @NotEmpty(message = "Pincode is required")
    @Pattern(regexp = "^[1-9][0-9]{5}$")
    private String pinCode;
}
