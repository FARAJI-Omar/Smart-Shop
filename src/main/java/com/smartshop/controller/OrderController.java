package com.smartshop.controller;

import com.smartshop.dto.request.OrderCreateDTO;
import com.smartshop.dto.response.OrderResponseDTO;
import com.smartshop.service.OrderService;
import com.smartshop.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDTO createOrder(@Valid @RequestBody OrderCreateDTO dto, HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        return orderService.createOrder(dto);
    }

    @PutMapping("/{id}/confirm")
    public OrderResponseDTO confirmOrder(@PathVariable Long id, HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        return orderService.confirmOrder(id);
    }

    @PutMapping("/{id}/cancel")
    public OrderResponseDTO cancelOrder(@PathVariable Long id, HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        return orderService.cancelOrder(id);
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id, HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        return orderService.getOrderById(id);
    }

    @GetMapping
    public java.util.List<OrderResponseDTO> getAllOrders(HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        return orderService.getAllOrders();
    }

    @GetMapping("/client/{clientId}")
    public java.util.List<OrderResponseDTO> getOrdersByClientId(@PathVariable Long clientId, HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        return orderService.getOrdersByClientId(clientId);
    }
}
