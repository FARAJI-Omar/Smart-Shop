package com.smartshop.service;

import com.smartshop.dto.response.PromoCodeResponseDTO;

import java.util.List;

public interface PromoCodeService {
    PromoCodeResponseDTO generatePromoCode();
    List<PromoCodeResponseDTO> getAllPromoCodes();
}

