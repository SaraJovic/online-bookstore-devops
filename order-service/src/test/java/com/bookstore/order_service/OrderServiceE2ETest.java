package com.bookstore.order_service;

import com.bookstore.order_service.dto.OrderRequest;
import com.bookstore.order_service.dto.OrderResponse;
import com.bookstore.order_service.model.OrderStatus;
import com.bookstore.order_service.repository.OrderRepository;
import com.bookstore.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceE2ETest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        doNothing().when(rabbitTemplate).convertAndSend(
                anyString(), anyString(), any(Object.class));
    }

    @Test
    void createAndRetrieveOrder() {
        OrderRequest request = new OrderRequest();
        request.setUserId(1L);
        request.setBookId(1L);
        request.setQuantity(2);
        request.setTotalPrice(new BigDecimal("59.98"));

        OrderResponse created = orderService.createOrder(request);

        assertNotNull(created.getId());
        assertEquals(OrderStatus.PENDING, created.getStatus());

        OrderResponse retrieved = orderService.getOrderById(created.getId());
        assertEquals(created.getId(), retrieved.getId());
    }

    @Test
    void createAndUpdateOrderStatus() {
        OrderRequest request = new OrderRequest();
        request.setUserId(1L);
        request.setBookId(2L);
        request.setQuantity(1);
        request.setTotalPrice(new BigDecimal("29.99"));

        OrderResponse created = orderService.createOrder(request);
        assertEquals(OrderStatus.PENDING, created.getStatus());

        OrderResponse updated = orderService.updateOrderStatus(created.getId(), OrderStatus.CONFIRMED);
        assertEquals(OrderStatus.CONFIRMED, updated.getStatus());
    }

    @Test
    void getOrdersByUserId() {
        OrderRequest request = new OrderRequest();
        request.setUserId(5L);
        request.setBookId(1L);
        request.setQuantity(1);
        request.setTotalPrice(new BigDecimal("19.99"));

        orderService.createOrder(request);

        List<OrderResponse> orders = orderService.getOrdersByUserId(5L);
        assertFalse(orders.isEmpty());
        assertEquals(5L, orders.get(0).getUserId());
    }
}