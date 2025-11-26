package com.smartshop.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {
    @NotBlank(message = "Product name is required")
    private String name;
    
    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    private Double unitPrice;
    
    @DecimalMin(value = "0.0", message = "Available stock must be greater than or equal to 0")
    private Double availableStock = 0.0;
}
