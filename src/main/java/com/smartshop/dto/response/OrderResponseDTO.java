package com.smartshop.dto.response;

import com.smartshop.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private ClientResponseDTO client;
    private List<OrderItemResponseDTO> items;
    private PromoCodeResponseDTO promoCode;
    private List<PaymentResponseDTO> payments;
    private LocalDateTime date;
    private Double subTotal;
    private Double discount;
    private Double tva;
    private Double total;
    private OrderStatus status;
    private Double remainingAmount;
}
