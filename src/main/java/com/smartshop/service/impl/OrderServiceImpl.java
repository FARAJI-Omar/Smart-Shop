package com.smartshop.service.impl;

import com.smartshop.dto.request.OrderCreateDTO;
import com.smartshop.dto.request.OrderItemCreateDTO;
import com.smartshop.dto.response.OrderResponseDTO;
import com.smartshop.entity.Client;
import com.smartshop.entity.Order;
import com.smartshop.entity.OrderItem;
import com.smartshop.entity.Product;
import com.smartshop.entity.PromoCode;
import com.smartshop.entity.enums.OrderStatus;
import com.smartshop.entity.enums.PaymentStatus;
import com.smartshop.mapper.OrderMapper;
import com.smartshop.repository.ClientRepository;
import com.smartshop.repository.OrderRepository;
import com.smartshop.repository.ProductRepository;
import com.smartshop.repository.PromoCodeRepository;
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
    private final PromoCodeRepository promoCodeRepository;
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

        // Apply loyalty discount
        double loyaltyDiscount = calculateLoyaltyDiscount(client, subtotal);

        // Apply promo code discount if provided
        double promoDiscount = 0.0;
        PromoCode promoCode = null;
        if (dto.getPromoCode() != null && !dto.getPromoCode().trim().isEmpty()) {
            promoCode = validateAndApplyPromoCode(dto.getPromoCode(), subtotal);
            if (promoCode != null) {
                promoDiscount = Math.round(subtotal * 0.05 * 100.0) / 100.0;
            }
        }

        // Total discount (loyalty + promo)
        double totalDiscount = loyaltyDiscount + promoDiscount;
        order.setDiscount(totalDiscount);
        order.setPromoCode(promoCode);

        // Calculate TVA on amount after discounts
        double amountAfterDiscount = subtotal - totalDiscount;
        order.setTva(amountAfterDiscount * tvaRate);
        order.setTotal(amountAfterDiscount + order.getTva());
        order.setRemainingAmount(order.getTotal());
        order.setStatus(stockSufficient ? OrderStatus.PENDING : OrderStatus.REJECTED);

        Order saved = orderRepository.save(order);

        // Mark promo code as used after successful order creation
        if (promoCode != null && stockSufficient) {
            promoCode.setIsUsed(true);
            promoCode.setOrder(saved);
            promoCodeRepository.save(promoCode);
        }

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

        // Check if order is fully paid
        if (order.getRemainingAmount() > 0) {
            throw new IllegalStateException(
                    String.format("Order must be fully paid before confirmation (remaining: %.2f DH)",
                    order.getRemainingAmount()));
        }

        // Check if there are any PENDING payments (CHECK or BANK_TRANSFER not yet confirmed)
        boolean hasPendingPayments = order.getPayments().stream()
                .anyMatch(p -> p.getStatus() == PaymentStatus.PENDING);

        if (hasPendingPayments) {
            throw new IllegalStateException("Order has pending payments, cannot confirm");
        }

        // Check stock availability
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

        // Decrease stock
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setAvailableStock(product.getAvailableStock() - item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CONFIRMED);
        Order saved = orderRepository.save(order);

        // TODO: Update client statistics

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

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return orderMapper.toDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderMapper.toListDTO(orderRepository.findAll());
    }

    @Override
    public List<OrderResponseDTO> getOrdersByClientId(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new EntityNotFoundException("Client not found");
        }
        return orderMapper.toListDTO(orderRepository.findClientsOrders(clientId));
    }

    /**
     * loyalty discount based on client's tier and subtotal
     * basic: 0% (no discount)
     * silver: 5% if subtotal ≥ 500
     * gold: 10% if subtotal ≥ 800
     * platinum: 15% if subtotal ≥ 1200
     */
    private double calculateLoyaltyDiscount(Client client, double subtotal) {
        if (client.getLoyaltyLevel() == null) {
            return 0.0;
        }

        double discountRate = switch (client.getLoyaltyLevel()) {
            case SILVER -> subtotal >= 500 ? 0.05 : 0.0;
            case GOLD -> subtotal >= 800 ? 0.10 : 0.0;
            case PLATINUM -> subtotal >= 1200 ? 0.15 : 0.0;
            case BASIC -> 0.0;
        };

        return Math.round(subtotal * discountRate * 100.0) / 100.0; // Round to 2 decimals
    }

    private PromoCode validateAndApplyPromoCode(String code, double subtotal) {
        PromoCode promoCode = promoCodeRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Promo code not found: " + code));

        if (promoCode.getIsUsed()) {
            throw new IllegalStateException("Promo code already used: " + code);
        }

        return promoCode;
    }
}
