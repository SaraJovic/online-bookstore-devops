package com.bookstore.order_service.repository;

import com.bookstore.order_service.model.Order;
import com.bookstore.order_service.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();

        order = Order.builder()
                .userId(1L)
                .bookId(1L)
                .quantity(2)
                .totalPrice(new BigDecimal("59.98"))
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(order);
    }

    @Test
    void findByUserId_Success() {
        List<Order> orders = orderRepository.findByUserId(1L);
        assertFalse(orders.isEmpty());
        assertEquals(1L, orders.get(0).getUserId());
    }

    @Test
    void findByStatus_Success() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.PENDING);
        assertFalse(orders.isEmpty());
        assertEquals(OrderStatus.PENDING, orders.get(0).getStatus());
    }

    @Test
    void findByUserIdAndStatus_Success() {
        List<Order> orders = orderRepository.findByUserIdAndStatus(1L, OrderStatus.PENDING);
        assertFalse(orders.isEmpty());
    }

    @Test
    void save_Success() {
        Order newOrder = Order.builder()
                .userId(2L)
                .bookId(2L)
                .quantity(1)
                .totalPrice(new BigDecimal("29.99"))
                .status(OrderStatus.PENDING)
                .build();

        Order saved = orderRepository.save(newOrder);
        assertNotNull(saved.getId());
    }
}