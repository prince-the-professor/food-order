package com.prince.order.entity;

import com.prince.order.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Accessors(chain = true)
public class Order extends Timestamp {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.ACCEPTED;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_person_id")
    private DeliveryPerson deliveryPerson;

    @Column(name = "order_amount")
    private double orderAmount;

    private double discount;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "preparation_time")
    private long preparationTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Order() {
    }

    public Order(String orderId, double orderAmount, double discount, String deliveryAddress, User user,
            long preparationTime) {
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.discount = discount;
        this.deliveryAddress = deliveryAddress;
        this.user = user;
        this.preparationTime = preparationTime;
    }

    public static class OrderBuilder {
        private String orderId;
        private double orderAmount;
        private long preparationTime;
        private double discount;
        private String deliveryAddress;
        private User user;

        public OrderBuilder setOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderBuilder setUser(User user) {
            this.user = user;
            return this;
        }

        public OrderBuilder setOrderAmount(double orderAmount) {
            this.orderAmount = orderAmount;
            return this;
        }

        public OrderBuilder setPreparationTime(long preparationTime) {
            this.preparationTime = preparationTime;
            return this;
        }

        public OrderBuilder setDiscount(double discount) {
            this.discount = discount;
            return this;
        }

        public OrderBuilder setDeliveryAddress(String deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
            return this;
        }

        public Order build() {
            Order order = new Order(orderId, orderAmount, discount, deliveryAddress, user, preparationTime);
            return order;
        }
    }
}

