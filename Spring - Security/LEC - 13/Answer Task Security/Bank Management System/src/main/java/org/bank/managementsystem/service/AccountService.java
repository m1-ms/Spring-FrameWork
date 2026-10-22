package org.bank.managementsystem.service;

import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.dto.AccountDto;
import org.bank.managementsystem.dto.CreateAccountRequest;
import org.bank.managementsystem.dto.UpdateAccountRequest;
import org.bank.managementsystem.entity.Account;
import org.bank.managementsystem.entity.Customer;
import org.bank.managementsystem.mapper.DtoMapper;
import org.bank.managementsystem.repository.AccountRepository;
import org.bank.managementsystem.repository.CustomerRepository;
import org.bank.managementsystem.security.CustomUserDetails;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final DtoMapper mapper;

    @PreAuthorize("hasAuthority('ACCOUNT_CREATE')")
    @Transactional
    public AccountDto create(CreateAccountRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        BigDecimal balance = (request.getInitialBalance() == null)
                ? BigDecimal.ZERO
                : request.getInitialBalance();

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .currency(request.getCurrency().toUpperCase())
                .balance(balance)
                .customer(customer)
                .build();

        return mapper.toAccountDto(accountRepository.save(account));
    }

    @PreAuthorize("hasAuthority('ACCOUNT_READ')")
    @Transactional(readOnly = true)
    public AccountDto getById(Long id, CustomUserDetails currentUser) {
        Account account = findOrThrow(id);

        if (!currentUser.isStaff() && !account.getCustomer().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot access this account");
        }

        return mapper.toAccountDto(account);
    }

    @PreAuthorize("hasAuthority('ACCOUNT_READ')")
    @Transactional(readOnly = true)
    public List<AccountDto> getMyAccounts(CustomUserDetails currentUser) {
        return accountRepository.findByCustomerId(currentUser.getId()).stream()
                .map(mapper::toAccountDto)
                .toList();
    }

    @PreAuthorize("hasAuthority('ACCOUNT_UPDATE')")
    @Transactional
    public AccountDto updateStatus(Long id, UpdateAccountRequest request) {
        Account account = findOrThrow(id);
        account.setStatus(request.getStatus());
        return mapper.toAccountDto(accountRepository.save(account));
    }

    @PreAuthorize("hasAuthority('ACCOUNT_DELETE')")
    @Transactional
    public void delete(Long id) {
        Account account = findOrThrow(id);

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Account balance must be zero before deletion");
        }

        try {
            accountRepository.delete(account);
            accountRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Account has transfers and cannot be deleted");
        }
    }

    private Account findOrThrow(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    private String generateAccountNumber() {
        String number;
        do {
            number = String.format("%012d", RANDOM.nextLong(1_000_000_000_000L));
        } while (accountRepository.existsByAccountNumber(number));
        return number;
    }
}