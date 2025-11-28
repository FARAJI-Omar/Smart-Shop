package com.smartshop.controller;

import com.smartshop.dto.response.PromoCodeResponseDTO;
import com.smartshop.service.PromoCodeService;
import com.smartshop.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promocode")
@RequiredArgsConstructor
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    @PostMapping("/generate")
    public ResponseEntity<PromoCodeResponseDTO> generatePromoCode(HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admins access required");
        }

        PromoCodeResponseDTO promoCode = promoCodeService.generatePromoCode();
        return ResponseEntity.status(HttpStatus.CREATED).body(promoCode);
    }


    @GetMapping
    public ResponseEntity<List<PromoCodeResponseDTO>> getAllPromoCodes(HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admins access required");
        }
        List<PromoCodeResponseDTO> promoCodes = promoCodeService.getAllPromoCodes();
        return ResponseEntity.ok(promoCodes);
    }
}

