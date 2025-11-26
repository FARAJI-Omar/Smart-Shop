package com.smartshop.mapper;

import com.smartshop.dto.response.OrderResponseDTO;
import com.smartshop.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponseDTO toDTO(Order order);
}
