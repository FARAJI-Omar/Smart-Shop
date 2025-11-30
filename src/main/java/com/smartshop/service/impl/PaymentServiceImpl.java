package com.smartshop.service.impl;

import com.smartshop.dto.request.PaymentCreateDTO;
import com.smartshop.dto.request.PaymentUpdateStatusDTO;
import com.smartshop.dto.response.PaymentResponseDTO;
import com.smartshop.entity.Order;
import com.smartshop.entity.Payment;
import com.smartshop.entity.enums.OrderStatus;
import com.smartshop.entity.enums.PaymentStatus;
import com.smartshop.entity.enums.PaymentType;
import com.smartshop.mapper.PaymentMapper;
import com.smartshop.repository.OrderRepository;
import com.smartshop.repository.PayementRepository;
import com.smartshop.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PayementRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponseDTO addPayment(PaymentCreateDTO dto) {
        // Validate order exists
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        // Validate order status is PENDING
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Order must be in PENDING status");
        }

        // Validate amount > 0
        if (dto.getAmount() <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        // Validate amount <= remainingAmount (no overpayment)
        if (dto.getAmount() > order.getRemainingAmount()) {
            throw new IllegalArgumentException(
                    String.format("Payment amount exceeds remaining amount (%.2f DH)",
                    order.getRemainingAmount()));
        }

        // Create payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(roundToTwoDecimals(dto.getAmount()));
        payment.setPaymentType(dto.getPaymentType());
        payment.setDatePayment(LocalDateTime.now());

        // Get next payment number for this order
        Integer nextPaymentNumber = paymentRepository.getNextPaymentNumber(order.getId());
        payment.setPaymentNumber(nextPaymentNumber);

        // Set status based on payment type
        if (dto.getPaymentType() == PaymentType.CASH) {
            // CASH = instant PAID
            payment.setStatus(PaymentStatus.PAID);
            payment.setDateReceipt(LocalDateTime.now());

            // Update order remaining amount immediately
            double newRemaining = order.getRemainingAmount() - payment.getAmount();
            order.setRemainingAmount(roundToTwoDecimals(newRemaining));
            orderRepository.save(order);
        } else {
            // CHEQUE or BANK_TRANSFER = PENDING
            payment.setStatus(PaymentStatus.PENDING);
            payment.setDateReceipt(null);
            // DO NOT update remainingAmount until admin confirms
        }

        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public PaymentResponseDTO updatePaymentStatus(Long paymentId, PaymentUpdateStatusDTO dto) {
        // Validate payment exists
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        // Validate payment type is not CASH
        if (payment.getPaymentType() == PaymentType.CASH) {
            throw new IllegalStateException("Cannot change status of CASH payment");
        }

        // Validate current status is PENDING
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Payment already processed");
        }

        // Validate new status is PAID or REJECTED
        if (dto.getStatus() != PaymentStatus.PAID && dto.getStatus() != PaymentStatus.REJECTED) {
            throw new IllegalArgumentException("Status must be PAID or REJECTED");
        }

        Order order = payment.getOrder();

        if (dto.getStatus() == PaymentStatus.PAID) {
            // Mark as PAID
            payment.setStatus(PaymentStatus.PAID);
            payment.setDateReceipt(LocalDateTime.now());

            // Update order remaining amount
            double newRemaining = order.getRemainingAmount() - payment.getAmount();
            order.setRemainingAmount(roundToTwoDecimals(newRemaining));
            orderRepository.save(order);
        } else {
            // Mark as REJECTED
            payment.setStatus(PaymentStatus.REJECTED);
            payment.setDateReceipt(null);
            // remainingAmount unchanged
        }

        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toDTO(saved);
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        return paymentMapper.toDTO(payment);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByOrderId(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found");
        }
        List<Payment> payments = paymentRepository.findByOrderIdOrderByPaymentNumberAsc(orderId);
        return paymentMapper.toDTO(payments);
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return paymentMapper.toDTO(payments);
    }

    /**
     * Round amount to 2 decimal places
     */
    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

