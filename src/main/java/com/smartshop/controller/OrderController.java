package com.smartshop.controller;

import com.smartshop.dto.request.OrderCreateDTO;
import com.smartshop.dto.response.OrderResponseDTO;
import com.smartshop.exception.UnauthorizedAccessException;
import com.smartshop.service.OrderService;
import com.smartshop.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDTO createOrder(
            @Valid @RequestBody OrderCreateDTO dto,
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Admin access required");
        }
        return orderService.createOrder(dto);
    }

    @PutMapping("/{id}/confirm")
    public OrderResponseDTO confirmOrder(
            @PathVariable Long id,
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Admin access required");
        }
        return orderService.confirmOrder(id);
    }

    @PutMapping("/{id}/cancel")
    public OrderResponseDTO cancelOrder(
            @PathVariable Long id,
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Admin access required");
        }
        return orderService.cancelOrder(id);
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(
            @PathVariable Long id,
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Admin access required");
        }
        return orderService.getOrderById(id);
    }

    @GetMapping
    public Page<OrderResponseDTO> getAllOrders
            (HttpServletRequest request,
             @RequestParam (defaultValue = "0") int page,
             @RequestParam (defaultValue = "10") int size) {
        if (!SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Admin access required");
        }
        return orderService.getAllOrders(PageRequest.of(page, size));
    }

    @GetMapping("/client/{clientId}")
    public Page<OrderResponseDTO> getOrdersByClientId(
            @PathVariable Long clientId,
            HttpServletRequest request,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size) {
        if (!SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Admin access required");
        }
        return orderService.getOrdersByClientId(clientId, PageRequest.of(page, size));
    }

    @GetMapping("/myorders")
    public Page<OrderResponseDTO> getMyOrders(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!SessionUtil.isClient(request)) {
            throw new UnauthorizedAccessException("Client access required");
        }

        Long userId = SessionUtil.getUserId(request);
        return orderService.getOrdersByClientId(userId, PageRequest.of(page, size));
    }
}

