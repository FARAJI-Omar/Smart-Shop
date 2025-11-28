package com.smartshop.dto.request;

import com.smartshop.entity.enums.CustomerTier;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateDTO {
    private String name;
    
    @Email(message = "Email is not valid")
    private String email;
    
    private CustomerTier loyaltyLevel;
}
