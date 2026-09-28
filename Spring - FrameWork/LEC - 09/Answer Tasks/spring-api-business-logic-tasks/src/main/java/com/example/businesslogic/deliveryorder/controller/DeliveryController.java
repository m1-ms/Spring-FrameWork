package com.example.businesslogic.deliveryorder.controller;

import com.example.businesslogic.deliveryorder.dto.AssignDriverRequest;
import com.example.businesslogic.deliveryorder.dto.CreateDeliveryRequest;
import com.example.businesslogic.deliveryorder.dto.DeliveryResponse;
import com.example.businesslogic.deliveryorder.dto.UpdateStatusRequest;
import com.example.businesslogic.deliveryorder.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public ResponseEntity<DeliveryResponse> create(@Valid @RequestBody CreateDeliveryRequest request) {
        return ResponseEntity.ok(deliveryService.create(request));
    }

    @PostMapping("/{deliveryId}/assign")
    public ResponseEntity<DeliveryResponse> assign(@PathVariable Long deliveryId,
                                                   @Valid @RequestBody AssignDriverRequest request) {
        return ResponseEntity.ok(deliveryService.assignDriver(deliveryId, request));
    }

    @PostMapping("/{deliveryId}/status")
    public ResponseEntity<DeliveryResponse> updateStatus(@PathVariable Long deliveryId,
                                                         @Valid @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(deliveryService.updateStatus(deliveryId, request));
    }

    @PostMapping("/{deliveryId}/cancel")
    public ResponseEntity<DeliveryResponse> cancel(@PathVariable Long deliveryId) {
        return ResponseEntity.ok(deliveryService.cancel(deliveryId));
    }
}
