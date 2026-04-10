package com.bookstore.order_service;

import com.bookstore.order_service.dto.OrderRequest;
import com.bookstore.order_service.dto.OrderResponse;
import com.bookstore.order_service.model.Order;
import com.bookstore.order_service.model.OrderStatus;
import com.bookstore.order_service.repository.OrderRepository;
import com.bookstore.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .id(1L)
                .userId(1L)
                .bookId(1L)
                .quantity(2)
                .totalPrice(new BigDecimal("59.98"))
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        orderRequest = new OrderRequest();
        orderRequest.setUserId(1L);
        orderRequest.setBookId(1L);
        orderRequest.setQuantity(2);
        orderRequest.setTotalPrice(new BigDecimal("59.98"));
    }

    @Test
    void createOrder_Success() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        doNothing().when(rabbitTemplate).convertAndSend(
                anyString(), anyString(), any(Object.class));

        OrderResponse response = orderService.createOrder(orderRequest);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(1L, response.getBookId());
        assertEquals(2, response.getQuantity());
        assertEquals(OrderStatus.PENDING, response.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.getOrderById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getOrderById_NotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.getOrderById(99L));
    }

    @Test
    void getAllOrders_Success() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderResponse> responses = orderService.getAllOrders();

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    void getOrdersByUserId_Success() {
        when(orderRepository.findByUserId(1L)).thenReturn(List.of(order));

        List<OrderResponse> responses = orderService.getOrdersByUserId(1L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    void updateOrderStatus_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.updateOrderStatus(1L, OrderStatus.CONFIRMED);

        assertNotNull(response);
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}