package com.smartshop.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smartshop.entity.enums.CustomerTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientStatisticsDTO {
    private Long clientId;
    private CustomerTier loyaltyLevel;
    private Integer totalOrders;
    private Integer totalConfirmedOrders;
    private Double totalSpent;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm")
    private LocalDateTime firstOrderDate;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm")
    private LocalDateTime lastOrderDate;
}

