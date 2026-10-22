package org.bank.managementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.dto.AssignRolesRequestDto;
import org.bank.managementsystem.dto.CreateCustomerRequestDto;
import org.bank.managementsystem.dto.CustomerDto;
import org.bank.managementsystem.security.CustomUserDetails;
import org.bank.managementsystem.service.AdminCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final AdminCustomerService adminCustomerService;

    @PostMapping
    public ResponseEntity<CustomerDto> create(@Valid @RequestBody CreateCustomerRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminCustomerService.create(request));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<CustomerDto> assignRoles(@PathVariable Long id, @Valid @RequestBody AssignRolesRequestDto request, @AuthenticationPrincipal CustomUserDetails admin) {
        return ResponseEntity.ok(adminCustomerService.assignRoles(id, request, admin));
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<CustomerDto> block(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails admin) {
        return ResponseEntity.ok(adminCustomerService.block(id, admin));
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<CustomerDto> unblock(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails admin) {
        return ResponseEntity.ok(adminCustomerService.unblock(id, admin));
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<CustomerDto> disable(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails admin) {
        return ResponseEntity.ok(adminCustomerService.disable(id, admin));
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<CustomerDto> enable(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails admin) {
        return ResponseEntity.ok(adminCustomerService.enable(id, admin));
    }
}