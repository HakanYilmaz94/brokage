package com.ing.brokage.service;

import com.ing.brokage.modal.dto.OrderDto;
import com.ing.brokage.modal.request.CreateOrderRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    void createOrder(CreateOrderRequest request, long customerId);
    void cancelOrder(Long orderId, long customerId);
    List<OrderDto> listOrders(long customerId, LocalDateTime startDate, LocalDateTime endDate);
    void matchOrder(Long orderId);
}
