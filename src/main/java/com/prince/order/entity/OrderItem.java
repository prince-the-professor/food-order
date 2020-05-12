package com.prince.order.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@Accessors(chain = true)
public class OrderItem extends Timestamp {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private long quantity;

    private double price;

    public OrderItem() {
    }
}
