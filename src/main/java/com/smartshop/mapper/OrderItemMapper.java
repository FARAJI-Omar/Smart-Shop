package com.smartshop.mapper;

import com.smartshop.dto.request.OrderItemCreateDTO;
import com.smartshop.dto.response.OrderItemResponseDTO;
import com.smartshop.entity.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItem toEntity(OrderItemCreateDTO dto);
    OrderItemResponseDTO toDTO(OrderItem item);
    List<OrderItemResponseDTO> toListDTO(List<OrderItem> items);
}
