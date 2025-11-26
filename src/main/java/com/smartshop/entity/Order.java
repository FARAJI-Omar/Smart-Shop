package com.smartshop.entity;

import com.smartshop.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime date;
    private Double subTotal;
    private Double discount;
    private Double tva;
    private Double total;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double remainingAmount;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @OneToOne
    @JoinColumn(name = "promo_code_id")
    private PromoCode promoCode;

    @OneToMany(mappedBy = "order")
    private List<Payment> payments;
}
