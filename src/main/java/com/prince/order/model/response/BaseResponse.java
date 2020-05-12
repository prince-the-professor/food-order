package com.prince.order.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = -3767681815596883L;
    private boolean success;
    private String message;
}
