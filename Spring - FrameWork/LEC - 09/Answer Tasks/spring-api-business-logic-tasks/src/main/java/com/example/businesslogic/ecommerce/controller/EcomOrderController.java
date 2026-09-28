package com.example.businesslogic.ecommerce.controller;

import com.example.businesslogic.ecommerce.dto.CreateOrderRequest;
import com.example.businesslogic.ecommerce.dto.OrderResponse;
import com.example.businesslogic.ecommerce.dto.UpdateOrderRequest;
import com.example.businesslogic.ecommerce.service.EcomOrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ecommerce/orders")
public class EcomOrderController {

    private final EcomOrderService orderService;

    public EcomOrderController(EcomOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long orderId,
                                                @Valid @RequestBody UpdateOrderRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, request));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirm(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.confirmOrder(orderId));
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderResponse> pay(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.payOrder(orderId));
    }
}
