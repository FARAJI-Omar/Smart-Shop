package com.smartshop.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDTO {
    @NotNull(message = "Client ID is required")
    private Long clientId;
    
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemCreateDTO> items;
    
    @Pattern(regexp = "^PROMO-[A-Z0-9]{4}$", message = "Promo code must follow format PROMO-XXXX")
    private String promoCode;
}
