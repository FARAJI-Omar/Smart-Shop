package com.smartshop.service;

import com.smartshop.dto.response.PromoCodeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PromoCodeService {
    PromoCodeResponseDTO generatePromoCode();
    Page<PromoCodeResponseDTO> getAllPromoCodes(Pageable pageable);
}

