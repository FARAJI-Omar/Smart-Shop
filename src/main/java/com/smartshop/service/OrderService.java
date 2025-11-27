package com.smartshop.service;

import com.smartshop.dto.request.OrderCreateDTO;
import com.smartshop.dto.response.OrderResponseDTO;

public interface OrderService {
    OrderResponseDTO createOrder(OrderCreateDTO dto);
    OrderResponseDTO confirmOrder(Long id);
    OrderResponseDTO cancelOrder(Long id);
}
