package com.smartshop.controller;

import com.smartshop.dto.request.PaymentCreateDTO;
import com.smartshop.dto.request.PaymentUpdateStatusDTO;
import com.smartshop.dto.response.PaymentResponseDTO;
import com.smartshop.service.PaymentService;
import com.smartshop.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> addPayment(
            @Valid
            @RequestBody PaymentCreateDTO dto,
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.addPayment(dto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(
            @PathVariable Long id,
            @Valid @RequestBody PaymentUpdateStatusDTO dto,
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, dto));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByOrderId(
            @PathVariable Long orderId,
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        return ResponseEntity.ok(paymentService.getPaymentsByOrderId(orderId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(
            @PathVariable Long id,
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments(
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}

