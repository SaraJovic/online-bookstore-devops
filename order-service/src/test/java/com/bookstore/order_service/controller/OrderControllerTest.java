package com.bookstore.order_service.controller;

import com.bookstore.order_service.dto.OrderRequest;
import com.bookstore.order_service.dto.OrderResponse;
import com.bookstore.order_service.model.OrderStatus;
import com.bookstore.order_service.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private OrderResponse orderResponse;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        orderResponse = OrderResponse.builder()
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
    void createOrder_Success() throws Exception {
        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(orderResponse);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void getAllOrders_Success() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(orderResponse));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getOrderById_Success() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(orderResponse);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void health_Success() throws Exception {
        mockMvc.perform(get("/api/orders/health"))
                .andExpect(status().isOk());
    }
}