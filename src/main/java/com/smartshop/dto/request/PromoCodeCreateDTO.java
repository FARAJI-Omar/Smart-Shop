package com.smartshop.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeCreateDTO {
    @NotBlank(message = "Promo code is required")
    @Pattern(regexp = "^PROMO-[A-Z0-9]{4}$", message = "Promo code must follow format PROMO-XXXX")
    private String code;
    
    @NotNull(message = "Percentage is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Percentage must be greater than 0")
    @DecimalMax(value = "100.0", message = "Percentage must not exceed 100")
    private Double percentage;
}
