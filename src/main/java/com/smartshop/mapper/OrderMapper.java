package com.smartshop.mapper;

import com.smartshop.dto.response.OrderResponseDTO;
import com.smartshop.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "promoCode.code", target = "promoCode")
    OrderResponseDTO toDTO(Order order);
    List<OrderResponseDTO> toListDTO(List<Order> orders);
}
