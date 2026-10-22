package org.bank.managementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.dto.CreateTransferRequest;
import org.bank.managementsystem.dto.TransferDto;
import org.bank.managementsystem.security.CustomUserDetails;
import org.bank.managementsystem.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferDto> create(@Valid @RequestBody CreateTransferRequest request,
                                              @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transferService.create(request, currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferDto> getById(@PathVariable Long id,
                                               @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(transferService.getById(id, currentUser));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<TransferDto> approve(@PathVariable Long id,
                                               @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(transferService.approve(id, currentUser));
    }
}