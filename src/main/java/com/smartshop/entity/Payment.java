package com.smartshop.entity;

import com.smartshop.entity.enums.PaymentStatus;
import com.smartshop.entity.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int paymentNumber;
    private Double amount;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private LocalDateTime datePayment;
    private LocalDateTime dateReceipt;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
