package com.prince.order.entity;

import com.prince.order.enums.Available;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "delivery_person")
@Getter
@Setter
@Accessors(chain = true)
public class DeliveryPerson extends Timestamp {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(name = "mobile_no", nullable = false, unique = true)
    private String mobileNo;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "is_available")
    private boolean isAvailable = true;

    @Column(name = "order_id")
    private String orderId;

    public DeliveryPerson() {
    }

}
