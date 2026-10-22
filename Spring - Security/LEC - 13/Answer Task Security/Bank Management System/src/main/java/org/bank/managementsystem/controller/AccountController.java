package org.bank.managementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.dto.AccountDto;
import org.bank.managementsystem.dto.CreateAccountRequest;
import org.bank.managementsystem.dto.UpdateAccountRequest;
import org.bank.managementsystem.security.CustomUserDetails;
import org.bank.managementsystem.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDto> create(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(request));
    }

    @GetMapping("/my-accounts")
    public ResponseEntity<List<AccountDto>> myAccounts(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(accountService.getMyAccounts(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getById(@PathVariable Long id,
                                              @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(accountService.getById(id, currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateStatus(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(accountService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}