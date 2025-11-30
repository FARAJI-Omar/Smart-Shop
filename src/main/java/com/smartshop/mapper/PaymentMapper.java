package com.smartshop.mapper;

import com.smartshop.dto.request.PaymentCreateDTO;
import com.smartshop.dto.response.PaymentResponseDTO;
import com.smartshop.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toEntity(PaymentCreateDTO dto);

    @Mapping(source = "order.id", target = "orderId")
    PaymentResponseDTO toDTO(Payment payment);

    List<PaymentResponseDTO> toDTO(List<Payment> payments);
}
