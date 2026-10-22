package org.bank.managementsystem.service;

import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.dto.CreateTransferRequest;
import org.bank.managementsystem.dto.TransferDto;
import org.bank.managementsystem.entity.Account;
import org.bank.managementsystem.entity.Customer;
import org.bank.managementsystem.entity.Transfer;
import org.bank.managementsystem.enums.AccountStatus;
import org.bank.managementsystem.enums.TransferStatus;
import org.bank.managementsystem.mapper.DtoMapper;
import org.bank.managementsystem.repository.AccountRepository;
import org.bank.managementsystem.repository.CustomerRepository;
import org.bank.managementsystem.repository.TransferRepository;
import org.bank.managementsystem.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final DtoMapper mapper;

    @PreAuthorize("hasAuthority('TRANSFER_CREATE')")
    @Transactional
    public TransferDto create(CreateTransferRequest request, CustomUserDetails currentUser) {
        BigDecimal amount = request.getAmount();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be greater than zero");
        }

        Account from = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Source account not found"));

        if (!from.getCustomer().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can only transfer from your own accounts");
        }

        Account to = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination account not found"));

        if (from.getId().equals(to.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Source and destination accounts cannot be the same");
        }

        if (from.getStatus() != AccountStatus.ACTIVE || to.getStatus() != AccountStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Both accounts must be ACTIVE");
        }

        if (!from.getCurrency().equals(to.getCurrency())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Both accounts must have the same currency");
        }

        if (from.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient balance");
        }

        Customer creator = customerRepository.getReferenceById(currentUser.getId());

        Transfer transfer = Transfer.builder()
                .fromAccount(from)
                .toAccount(to)
                .amount(amount)
                .createdBy(creator)
                .build();

        return mapper.toTransferDto(transferRepository.save(transfer));
    }

    @PreAuthorize("hasAuthority('TRANSFER_READ')")
    @Transactional(readOnly = true)
    public TransferDto getById(Long id, CustomUserDetails currentUser) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found"));

        if (!currentUser.isStaff() && !isParticipant(transfer, currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot access this transfer");
        }

        return mapper.toTransferDto(transfer);
    }

    @PreAuthorize("hasAuthority('TRANSFER_APPROVE')")
    @Transactional
    public TransferDto approve(Long id, CustomUserDetails approver) {
        Transfer transfer = transferRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found"));

        if (transfer.getStatus() != TransferStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Only PENDING transfers can be approved");
        }

        if (transfer.getCreatedBy().getId().equals(approver.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You cannot approve a transfer you created");
        }

        Long fromId = transfer.getFromAccount().getId();
        Long toId = transfer.getToAccount().getId();

        Long firstId = Math.min(fromId, toId);
        Long secondId = Math.max(fromId, toId);

        Account first = lockAccount(firstId);
        Account second = lockAccount(secondId);

        Account from = firstId.equals(fromId) ? first : second;
        Account to = (from == first) ? second : first;

        if (from.getStatus() != AccountStatus.ACTIVE || to.getStatus() != AccountStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Both accounts must be ACTIVE");
        }

        BigDecimal amount = transfer.getAmount();

        if (from.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient balance");
        }

        transfer.setStatus(TransferStatus.APPROVED);

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        transfer.setStatus(TransferStatus.COMPLETED);

        return mapper.toTransferDto(transfer);
    }

    private Account lockAccount(Long id) {
        return accountRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    private boolean isParticipant(Transfer transfer, Long customerId) {
        return transfer.getCreatedBy().getId().equals(customerId)
                || transfer.getFromAccount().getCustomer().getId().equals(customerId)
                || transfer.getToAccount().getCustomer().getId().equals(customerId);
    }
}