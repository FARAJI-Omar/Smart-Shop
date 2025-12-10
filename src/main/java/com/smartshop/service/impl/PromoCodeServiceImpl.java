package com.smartshop.service.impl;

import com.smartshop.dto.response.PromoCodeResponseDTO;
import com.smartshop.entity.PromoCode;
import com.smartshop.mapper.PromoCodeMapper;
import com.smartshop.repository.PromoCodeRepository;
import com.smartshop.service.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeMapper promoCodeMapper;
    private static final Double PROMO_DISCOUNT_PERCENTAGE = 5.0;
    private static final String PROMO_PREFIX = "PROMO-";
    private static final Random random = new Random();

    @Override
    @Transactional
    public PromoCodeResponseDTO generatePromoCode() {
        String code;

        // generate unique code (check if it doesnt already exist in DB)
        do {
            code = PROMO_PREFIX + generateCode();
        } while (promoCodeRepository.existsByCode(code));

        PromoCode promoCode = new PromoCode();
        promoCode.setCode(code);
        promoCode.setPercentage(PROMO_DISCOUNT_PERCENTAGE);
        promoCode.setIsUsed(false);

        PromoCode saved = promoCodeRepository.save(promoCode);
        return promoCodeMapper.toDTO(saved);
    }

    @Override
    public Page<PromoCodeResponseDTO> getAllPromoCodes(Pageable pageable) {
        Page<PromoCode> promoCodes = promoCodeRepository.findAll(pageable);
        return promoCodes.map(promoCodeMapper::toDTO);
    }

    // helper method: generate 4 random digits for promo code
    private String generateCode() {
        int number = random.nextInt(10000); // 0 to 9999
        return String.format("%04d", number); // Pad with zeros if needed
    }
}

