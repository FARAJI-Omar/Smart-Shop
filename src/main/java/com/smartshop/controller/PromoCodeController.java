package com.smartshop.controller;

import com.smartshop.dto.response.PromoCodeResponseDTO;
import com.smartshop.exception.UnauthorizedAccessException;
import com.smartshop.service.PromoCodeService;
import com.smartshop.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<PromoCodeResponseDTO> generatePromoCode(
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Admins access required");
        }

        PromoCodeResponseDTO promoCode = promoCodeService.generatePromoCode();
        return ResponseEntity.status(HttpStatus.CREATED).body(promoCode);
    }


    @GetMapping
    public ResponseEntity<Page<PromoCodeResponseDTO>> getAllPromoCodes(
            HttpServletRequest request,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size) {
        if (!SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Admins access required");
        }
        Page<PromoCodeResponseDTO> promoCodes = promoCodeService.getAllPromoCodes(PageRequest.of(page, size));
        return ResponseEntity.ok(promoCodes);
    }
}

