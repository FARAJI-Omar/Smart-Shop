package com.smartshop.service;

import com.smartshop.dto.request.PaymentCreateDTO;
import com.smartshop.dto.request.PaymentUpdateStatusDTO;
import com.smartshop.dto.response.PaymentResponseDTO;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO addPayment(PaymentCreateDTO dto);
    PaymentResponseDTO updatePaymentStatus(Long paymentId, PaymentUpdateStatusDTO dto);
    PaymentResponseDTO getPaymentById(Long id);
    List<PaymentResponseDTO> getPaymentsByOrderId(Long orderId);
    List<PaymentResponseDTO> getAllPayments();

    // total amount of payments PAID of an order
    Double montantTotal(Long orderId);
}

