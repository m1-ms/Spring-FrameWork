package com.example.businesslogic.productdiscount.controller;

import com.example.businesslogic.productdiscount.dto.PriceRequest;
import com.example.businesslogic.productdiscount.dto.PriceResponse;
import com.example.businesslogic.productdiscount.service.PriceCalculationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pricing")
public class PriceController {

    private final PriceCalculationService priceCalculationService;

    public PriceController(PriceCalculationService priceCalculationService) {
        this.priceCalculationService = priceCalculationService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<PriceResponse> calculate(@Valid @RequestBody PriceRequest request) {
        return ResponseEntity.ok(priceCalculationService.calculate(request));
    }
}
