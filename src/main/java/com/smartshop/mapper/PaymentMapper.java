package com.smartshop.mapper;

import com.smartshop.dto.request.PaymentCreateDTO;
import com.smartshop.dto.response.PaymentResponseDTO;
import com.smartshop.entity.Payment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toEntity(PaymentCreateDTO dto);
    PaymentResponseDTO toDTO(Payment payment);
    List<PaymentResponseDTO> toDTO(List<Payment> payments);
}
