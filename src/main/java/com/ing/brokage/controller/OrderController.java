package com.ing.brokage.controller;

import com.ing.brokage.modal.dto.OrderDto;
import com.ing.brokage.modal.request.CreateOrderRequest;
import com.ing.brokage.persistance.entity.Customer;
import com.ing.brokage.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/create")
    public ResponseEntity<Void> createOrder(@AuthenticationPrincipal Customer  customer, @Valid @RequestBody CreateOrderRequest request) {
        orderService.createOrder(request, customer.getId());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<Void> cancelOrder(@AuthenticationPrincipal Customer  customer, @Valid @PathVariable long orderId) {
        orderService.cancelOrder(orderId, customer.getId());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/list")
    public List<OrderDto> listOrders(@AuthenticationPrincipal Customer  customer,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        if (startDate == null) startDate = LocalDateTime.MIN;
        if (endDate == null) endDate = LocalDateTime.MAX;

        return orderService.listOrders(customer.getId(), startDate, endDate);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/match/{orderId}")
    public ResponseEntity<Void> matchOrder(@PathVariable long orderId) {
        orderService.matchOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
