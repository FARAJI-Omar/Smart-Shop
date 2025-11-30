package com.smartshop.dto.request;

import com.smartshop.entity.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateStatusDTO {
    @NotNull(message = "Payment status is required")
    private PaymentStatus status;
}

