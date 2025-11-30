package com.smartshop.service;

import com.smartshop.dto.request.OrderCreateDTO;
import com.smartshop.dto.response.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderCreateDTO dto);
    OrderResponseDTO confirmOrder(Long id);
    OrderResponseDTO cancelOrder(Long id);
    OrderResponseDTO getOrderById(Long id);
    Page<OrderResponseDTO> getAllOrders(Pageable pageable);
    Page<OrderResponseDTO> getOrdersByClientId(Long clientId, Pageable pageable);
}
