package com.smartshop.service.impl;

import com.smartshop.dto.request.OrderCreateDTO;
import com.smartshop.dto.request.OrderItemCreateDTO;
import com.smartshop.dto.response.OrderResponseDTO;
import com.smartshop.entity.Client;
import com.smartshop.entity.Order;
import com.smartshop.entity.OrderItem;
import com.smartshop.entity.Product;
import com.smartshop.entity.enums.OrderStatus;
import com.smartshop.mapper.OrderMapper;
import com.smartshop.repository.ClientRepository;
import com.smartshop.repository.OrderRepository;
import com.smartshop.repository.ProductRepository;
import com.smartshop.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Value("${app.config.tva-rate}")
    private Double tvaRate;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderCreateDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Order order = new Order();
        order.setClient(client);
        order.setDate(LocalDateTime.now());
        order.setDiscount(0.0);

        List<OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0.0;
        boolean stockSufficient = true;
        String rejectionMessage = null;

        for (OrderItemCreateDTO itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (itemDto.getQuantity() > product.getAvailableStock()) {
                stockSufficient = false;
                rejectionMessage = "Order is REJECTED, Insufficient stock for product " + product.getName();
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(product.getUnitPrice());
            orderItem.setLineTotal(itemDto.getQuantity() * product.getUnitPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
            subtotal += orderItem.getLineTotal();
        }

        order.setItems(orderItems);
        order.setSubTotal(subtotal);
        order.setTva(subtotal * tvaRate);
        order.setTotal(subtotal + order.getTva());
        order.setRemainingAmount(order.getTotal());
        order.setStatus(stockSufficient ? OrderStatus.PENDING : OrderStatus.REJECTED);

        Order saved = orderRepository.save(order);
        OrderResponseDTO response = orderMapper.toDTO(saved);
        
        if (!stockSufficient) {
            response.setMessage(rejectionMessage);
        }
        
        return response;
    }

    @Override
    @Transactional
    public OrderResponseDTO confirmOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found!"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only PENDING orders can be confirmed!");
        }

        if (order.getRemainingAmount() > 0) {
            throw new IllegalStateException("Order is not fully paid yet!");
        }

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            if (item.getQuantity() > product.getAvailableStock()) {
                order.setStatus(OrderStatus.REJECTED);
                Order saved = orderRepository.save(order);
                OrderResponseDTO response = orderMapper.toDTO(saved);
                response.setMessage("Order is REJECTED, Insufficient stock for product " + product.getName());
                return response;
            }
        }

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setAvailableStock(product.getAvailableStock() - item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CONFIRMED);
        Order saved = orderRepository.save(order);
        return orderMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only PENDING orders can be canceled");
        }

        order.setStatus(OrderStatus.CANCELED);
        Order saved = orderRepository.save(order);
        return orderMapper.toDTO(saved);
    }
}
