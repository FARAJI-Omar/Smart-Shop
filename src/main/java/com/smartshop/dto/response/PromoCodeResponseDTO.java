package com.smartshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeResponseDTO {
    private Long id;
    private String code;
    private Double percentage;
    private Boolean used;
}
