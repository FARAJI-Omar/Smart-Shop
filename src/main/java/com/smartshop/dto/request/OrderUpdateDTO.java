package com.smartshop.dto.request;

import com.smartshop.entity.enums.OrderStatus;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDTO {
    private OrderStatus status;
    
    @Pattern(regexp = "^PROMO-[A-Z0-9]{4}$", message = "Promo code must follow format PROMO-XXXX")
    private String promoCode;
}
