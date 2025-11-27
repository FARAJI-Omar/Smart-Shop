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
}
