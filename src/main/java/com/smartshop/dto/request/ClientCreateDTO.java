package com.smartshop.dto.request;

import com.smartshop.entity.enums.CustomerTier;
import jakarta.validation.constraints.Email;
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
public class ClientCreateDTO {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private CustomerTier loyaltyLevel;
}
