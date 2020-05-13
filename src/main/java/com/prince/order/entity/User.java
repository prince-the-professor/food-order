package com.prince.order.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_details")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class User extends Timestamp {

    @Id
    @NotNull
    @Column(name = "mobile_no", nullable = false, length = 10)
    private String mobileNo;

    @Column(name = "user_name")
    private String userName;

    @NonNull
    @Column(name = "address", columnDefinition = "JSON")
    private String address;

}
