package com.prince.order.entity;

import com.prince.order.enums.ItemCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "items")
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Item extends Timestamp {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "item_id", nullable = false, unique = true)
    private String itemId;

    @Column(name = "item_name")
    private String name;

    private double price;

    //discount in percentage
    @Column(name = "percentage_discount")
    private double percentageDiscount;

    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @Column(name = "is_available")
    private boolean isAvailable = true;

    @Column(name = "preparation_time")
    private long preparationTime;

    private String description;

    public double getDiscountValue() {
        return (price * percentageDiscount) / 100;
    }

    public Item() {
    }
}
