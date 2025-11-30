package com.smartshop.service.impl;

import com.smartshop.dto.response.OrderResponseDTO;
import com.smartshop.entity.*;
import com.smartshop.entity.enums.CustomerTier;
import com.smartshop.entity.enums.OrderStatus;
import com.smartshop.entity.enums.PaymentStatus;
import com.smartshop.entity.enums.PaymentType;
import com.smartshop.exception.*;
import com.smartshop.mapper.OrderMapper;
import com.smartshop.mapper.ProductMapper;
import com.smartshop.repository.ClientRepository;
import com.smartshop.repository.OrderRepository;
import com.smartshop.repository.ProductRepository;
import com.smartshop.repository.PromoCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Client mockClient;
    private Product mockProduct1;
    private Product mockProduct2;

    @BeforeEach
    void setUp() {
        mockClient = createMockClient(1L, "Test Client", "test@example.com", CustomerTier.SILVER);
        mockProduct1 = createMockProduct(1L, "Product 1", 10.0, 100.0);
        mockProduct2 = createMockProduct(2L, "Product 2", 20.0, 50.0);
    }

    // ========================================================================
    // PART 1: confirmOrder()
    // ========================================================================

    // ------------------------------------------------------------------------
    // A. SUCCESS SCENARIOS
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("Test 1: confirmOrder - Valid pending order with full payment should confirm order")
    void confirmOrder_WithValidPendingOrderAndFullPayment_ShouldConfirmOrder() {
        Long orderId = 1L;
        Order order = createMockOrder(orderId, mockClient, OrderStatus.PENDING, 0.0);

        // Add 2 items with sufficient stock
        OrderItem item1 = createMockOrderItem(1L, mockProduct1, 5);
        OrderItem item2 = createMockOrderItem(2L, mockProduct2, 3);
        order.setItems(Arrays.asList(item1, item2));

        // Add PAID payments (no PENDING)
        Payment payment = createMockPayment(1L, PaymentType.CASH, PaymentStatus.PAID, 156.0);
        order.setPayments(Arrays.asList(payment));

        Order savedOrder = createMockOrder(orderId, mockClient, OrderStatus.CONFIRMED, 0.0);
        savedOrder.setItems(order.getItems());

        OrderResponseDTO expectedResponse = new OrderResponseDTO();
        expectedResponse.setStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct1, mockProduct2);
        when(orderMapper.toDTO(savedOrder)).thenReturn(expectedResponse);

        // act
        OrderResponseDTO result = orderService.confirmOrder(orderId);

        // assert
        assertNotNull(result);
        assertEquals(OrderStatus.CONFIRMED, result.getStatus());

        // Verify order status changed to CONFIRMED
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(orderCaptor.capture());
        assertEquals(OrderStatus.CONFIRMED, orderCaptor.getValue().getStatus());

        // Verify stock decreased
        verify(productRepository, times(2)).save(any(Product.class));
        assertEquals(95.0, mockProduct1.getAvailableStock()); // 100 - 5
        assertEquals(47.0, mockProduct2.getAvailableStock()); // 50 - 3

        // Verify mapper called
        verify(orderMapper, times(1)).toDTO(savedOrder);
    }

    @Test
    @DisplayName("Test 2: confirmOrder - Order with multiple payment types should confirm order")
    void confirmOrder_WithMultiplePaymentTypes_ShouldConfirmOrder() {
        Long orderId = 2L;
        Order order = createMockOrder(orderId, mockClient, OrderStatus.PENDING, 0.0);

        OrderItem item1 = createMockOrderItem(1L, mockProduct1, 2);
        order.setItems(Arrays.asList(item1));

        // Multiple payment types: CASH + CHEQUE (both PAID)
        Payment cashPayment = createMockPayment(1L, PaymentType.CASH, PaymentStatus.PAID, 100.0);
        Payment checkPayment = createMockPayment(2L, PaymentType.CHEQUE, PaymentStatus.PAID, 56.0);
        order.setPayments(Arrays.asList(cashPayment, checkPayment));

        Order savedOrder = createMockOrder(orderId, mockClient, OrderStatus.CONFIRMED, 0.0);
        OrderResponseDTO expectedResponse = new OrderResponseDTO();
        expectedResponse.setStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct1);
        when(orderMapper.toDTO(savedOrder)).thenReturn(expectedResponse);

        // act
        OrderResponseDTO result = orderService.confirmOrder(orderId);

        // assert
        assertNotNull(result);
        assertEquals(OrderStatus.CONFIRMED, result.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productRepository, times(1)).save(mockProduct1);
    }

    // ------------------------------------------------------------------------
    // B. FAILURE SCENARIOS - Exceptions
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("Test 3: confirmOrder - Non-existent order should throw OrderNotFoundException")
    void confirmOrder_WithNonExistentOrder_ShouldThrowOrderNotFoundException() {
        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // act&assert
        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.confirmOrder(orderId)
        );

        assertEquals("Order not found!", exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Test 4: confirmOrder - Already confirmed order should throw OrderNotPendingException")
    void confirmOrder_WithConfirmedOrder_ShouldThrowOrderNotPendingException() {
        Long orderId = 4L;
        Order order = createMockOrder(orderId, mockClient, OrderStatus.CONFIRMED, 0.0);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // act&assert
        OrderNotPendingException exception = assertThrows(
                OrderNotPendingException.class,
                () -> orderService.confirmOrder(orderId)
        );

        assertEquals("Only PENDING orders can be confirmed!", exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Test 5: confirmOrder - Partial payment should throw OrderNotFullyPaidException")
    void confirmOrder_WithPartialPayment_ShouldThrowOrderNotFullyPaidException() {
        Long orderId = 5L;
        Order order = createMockOrder(orderId, mockClient, OrderStatus.PENDING, 500.0);
        order.setTotal(1000.0);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // act&assert
        OrderNotFullyPaidException exception = assertThrows(
                OrderNotFullyPaidException.class,
                () -> orderService.confirmOrder(orderId)
        );

        assertTrue(exception.getMessage().contains("Order must be fully paid before confirmation"));
        assertTrue(exception.getMessage().contains("500"));
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Test 6: confirmOrder - Pending CHECK payment should throw OrderHasPendingPaymentsException")
    void confirmOrder_WithPendingCheckPayment_ShouldThrowOrderHasPendingPaymentsException() {
        Long orderId = 6L;
        Order order = createMockOrder(orderId, mockClient, OrderStatus.PENDING, 0.0);

        // CHEQUE payment with PENDING status
        Payment pendingPayment = createMockPayment(1L, PaymentType.CHEQUE, PaymentStatus.PENDING, 156.0);
        order.setPayments(Arrays.asList(pendingPayment));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // act&assert
        OrderHasPendingPaymentsException exception = assertThrows(
                OrderHasPendingPaymentsException.class,
                () -> orderService.confirmOrder(orderId)
        );

        assertEquals("Order has pending payments, cannot confirm", exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    // ------------------------------------------------------------------------
    // C. STOCK REJECTION SCENARIOS
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("Test 7: confirmOrder - Insufficient stock should reject order")
    void confirmOrder_WithInsufficientStockForOneItem_ShouldRejectOrder() {
        Long orderId = 7L;
        Order order = createMockOrder(orderId, mockClient, OrderStatus.PENDING, 0.0);

        // Product 1: OK stock, Product 2: INSUFFICIENT stock
        OrderItem item1 = createMockOrderItem(1L, mockProduct1, 5); // OK: 100 >= 5

        Product insufficientProduct = createMockProduct(2L, "Product 2", 20.0, 2.0);
        OrderItem item2 = createMockOrderItem(2L, insufficientProduct, 10); // INSUFFICIENT: 2 < 10

        order.setItems(Arrays.asList(item1, item2));

        Payment payment = createMockPayment(1L, PaymentType.CASH, PaymentStatus.PAID, 156.0);
        order.setPayments(Arrays.asList(payment));

        Order rejectedOrder = createMockOrder(orderId, mockClient, OrderStatus.REJECTED, 0.0);
        OrderResponseDTO expectedResponse = new OrderResponseDTO();
        expectedResponse.setStatus(OrderStatus.REJECTED);
        expectedResponse.setMessage("Order is REJECTED, Insufficient stock for product Product 2");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(rejectedOrder);
        when(orderMapper.toDTO(rejectedOrder)).thenReturn(expectedResponse);

        // act
        OrderResponseDTO result = orderService.confirmOrder(orderId);

        // assert
        assertNotNull(result);
        assertEquals(OrderStatus.REJECTED, result.getStatus());
        assertTrue(result.getMessage().contains("Insufficient stock"));

        // Verify order status changed to REJECTED
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(orderCaptor.capture());
        assertEquals(OrderStatus.REJECTED, orderCaptor.getValue().getStatus());

        // Verify NO stock was decreased
        verify(productRepository, never()).save(any(Product.class));
        assertEquals(100.0, mockProduct1.getAvailableStock()); // No change
        assertEquals(2.0, insufficientProduct.getAvailableStock()); // No change
    }

    // ========================================================================
    // PART 2: cancelOrder()
    // ========================================================================

    // ------------------------------------------------------------------------
    // A. SUCCESS SCENARIOS
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("Test 8: cancelOrder - Valid pending order should cancel order")
    void cancelOrder_WithValidPendingOrder_ShouldCancelOrder() {
        Long orderId = 8L;
        Order order = createMockOrder(orderId, mockClient, OrderStatus.PENDING, 500.0);

        // Order has payments but is still PENDING
        Payment cashPayment = createMockPayment(1L, PaymentType.CASH, PaymentStatus.PAID, 50.0);
        Payment checkPayment = createMockPayment(2L, PaymentType.CHEQUE, PaymentStatus.PAID, 50.0);
        order.setPayments(Arrays.asList(cashPayment, checkPayment));

        Order canceledOrder = createMockOrder(orderId, mockClient, OrderStatus.CANCELED, 500.0);
        OrderResponseDTO expectedResponse = new OrderResponseDTO();
        expectedResponse.setStatus(OrderStatus.CANCELED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(canceledOrder);
        when(orderMapper.toDTO(canceledOrder)).thenReturn(expectedResponse);

        // act
        OrderResponseDTO result = orderService.cancelOrder(orderId);

        // assert
        assertNotNull(result);
        assertEquals(OrderStatus.CANCELED, result.getStatus());

        // Verify order status changed to CANCELED
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(orderCaptor.capture());
        assertEquals(OrderStatus.CANCELED, orderCaptor.getValue().getStatus());

        // Verify mapper called
        verify(orderMapper, times(1)).toDTO(canceledOrder);
    }

    // ------------------------------------------------------------------------
    // B. FAILURE SCENARIOS - Exceptions
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("Test 9: cancelOrder - Non-existent order should throw OrderNotFoundException")
    void cancelOrder_WithNonExistentOrder_ShouldThrowOrderNotFoundException() {
        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // act&assert
        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.cancelOrder(orderId)
        );

        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Test 10: cancelOrder - Confirmed order should throw OrderNotPendingException")
    void cancelOrder_WithConfirmedOrder_ShouldThrowOrderNotPendingException() {
        Long orderId = 10L;
        Order order = createMockOrder(orderId, mockClient, OrderStatus.CONFIRMED, 0.0);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // act&assert
        OrderNotPendingException exception = assertThrows(
                OrderNotPendingException.class,
                () -> orderService.cancelOrder(orderId)
        );

        assertEquals("Only PENDING orders can be canceled", exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    // ========================================================================
    // TEST DATA
    // ========================================================================

    private Client createMockClient(Long id, String name, String email, CustomerTier loyaltyLevel) {
        Client client = new Client();
        client.setId(id);
        client.setName(name);
        client.setEmail(email);
        client.setLoyaltyLevel(loyaltyLevel);
        return client;
    }

    private Product createMockProduct(Long id, String name, Double unitPrice, Double availableStock) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setUnitPrice(unitPrice);
        product.setAvailableStock(availableStock);
        return product;
    }

    private Order createMockOrder(Long id, Client client, OrderStatus status, Double remainingAmount) {
        Order order = new Order();
        order.setId(id);
        order.setClient(client);
        order.setStatus(status);
        order.setRemainingAmount(remainingAmount);
        order.setDate(LocalDateTime.now());
        order.setSubTotal(130.0);
        order.setDiscount(0.0);
        order.setTva(26.0);
        order.setTotal(156.0);
        order.setItems(new ArrayList<>());
        order.setPayments(new ArrayList<>());
        return order;
    }

    private OrderItem createMockOrderItem(Long id, Product product, Integer quantity) {
        OrderItem item = new OrderItem();
        item.setId(id);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPrice(product.getUnitPrice());
        item.setLineTotal(product.getUnitPrice() * quantity);
        return item;
    }

    private Payment createMockPayment(Long id, PaymentType type, PaymentStatus status, Double amount) {
        Payment payment = new Payment();
        payment.setId(id);
        payment.setPaymentType(type);
        payment.setStatus(status);
        payment.setAmount(amount);
        payment.setDatePayment(LocalDateTime.now());
        if (status == PaymentStatus.PAID) {
            payment.setDateReceipt(LocalDateTime.now());
        }
        return payment;
    }
}

