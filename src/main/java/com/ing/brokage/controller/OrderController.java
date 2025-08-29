package com.ing.brokage.controller;

import com.ing.brokage.annotation.IsAdmin;
import com.ing.brokage.annotation.IsUserOrAdmin;
import com.ing.brokage.modal.dto.OrderDto;
import com.ing.brokage.modal.request.CreateOrderRequest;
import com.ing.brokage.persistance.entity.Customer;
import com.ing.brokage.service.OrderService;
import com.ing.brokage.util.CustomerUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.ing.brokage.constant.UrlConstants.*;

@RestController
@RequestMapping(path = ORDER)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @IsUserOrAdmin
    @PostMapping(CREATE)
    public ResponseEntity<Void> createOrder(@AuthenticationPrincipal Customer  customer, @Valid @RequestBody CreateOrderRequest request) {
        orderService.createOrder(request, CustomerUtils.resolveCustomerId(customer, request.getCustomerId()));
        return ResponseEntity.ok().build();
    }

    @IsUserOrAdmin
    @PostMapping(CANCEL + ORDER_ID)
    public ResponseEntity<Void> cancelOrder(@AuthenticationPrincipal Customer  customer, @Valid @PathVariable long orderId, @RequestParam(required = false) Long customerId) {
        orderService.cancelOrder(orderId, CustomerUtils.resolveCustomerId(customer, customerId));
        return ResponseEntity.ok().build();
    }

    @IsUserOrAdmin
    @GetMapping(LIST)
    public List<OrderDto> listOrders(@AuthenticationPrincipal Customer  customer,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                     @RequestParam(required = false) Long customerId) {

        if (startDate == null) startDate = LocalDateTime.MIN;
        if (endDate == null) endDate = LocalDateTime.MAX;

        return orderService.listOrders(CustomerUtils.resolveCustomerId(customer, customerId), startDate, endDate);
    }

    @IsAdmin
    @PostMapping(MATCH + ORDER_ID)
    public ResponseEntity<Void> matchOrder(@PathVariable long orderId) {
        orderService.matchOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
