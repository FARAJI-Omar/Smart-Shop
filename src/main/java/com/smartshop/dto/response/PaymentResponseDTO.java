package com.smartshop.dto.response;

import com.smartshop.entity.enums.PaymentStatus;
import com.smartshop.entity.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long id;
    private int paymentNumber;
    private Double amount;
    private PaymentType paymentType;
    private PaymentStatus status;
    private LocalDateTime datePayment;
    private LocalDateTime dateReceipt;
}
