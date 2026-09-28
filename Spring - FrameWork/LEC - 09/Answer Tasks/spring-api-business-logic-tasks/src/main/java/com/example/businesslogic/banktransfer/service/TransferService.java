package com.example.businesslogic.banktransfer.service;

import com.example.businesslogic.banktransfer.dto.TransferRequest;
import com.example.businesslogic.banktransfer.dto.TransferResponse;
import com.example.businesslogic.banktransfer.entity.Account;
import com.example.businesslogic.banktransfer.entity.Transfer;
import com.example.businesslogic.banktransfer.enums.AccountStatus;
import com.example.businesslogic.banktransfer.enums.TransferStatus;
import com.example.businesslogic.banktransfer.repository.AccountRepository;
import com.example.businesslogic.banktransfer.repository.TransferRepository;
import com.example.businesslogic.common.exception.BusinessException;
import com.example.businesslogic.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TransferService {

    // Transfers above this amount need additional verification
    private static final BigDecimal VERIFICATION_THRESHOLD = new BigDecimal("5000");

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public TransferService(AccountRepository accountRepository,
                           TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        // Rule 1: sender and receiver must be different
        if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
            throw new BusinessException("Sender and receiver must be different accounts");
        }

        // Rule 2: both accounts must exist
        Account from = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Sender account not found"));
        Account to = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

        // Rule 3: both accounts must be active
        if (from.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Sender account is not active");
        }
        if (to.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Receiver account is not active");
        }

        BigDecimal amount = request.getAmount();

        // Rule 4: sender must have enough balance
        if (from.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Insufficient balance", HttpStatus.CONFLICT);
        }

        // Rule 5: daily limit depends on the account type
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        BigDecimal transferredToday =
                transferRepository.sumTransferredBetween(from.getId(), startOfDay, endOfDay);
        BigDecimal dailyLimit = from.getType().getDailyLimit();

        if (transferredToday.add(amount).compareTo(dailyLimit) > 0) {
            throw new BusinessException("Daily transfer limit exceeded", HttpStatus.CONFLICT);
        }

        // All checks passed, now build the transfer
        Transfer transfer = new Transfer();
        transfer.setFromAccount(from);
        transfer.setToAccount(to);
        transfer.setAmount(amount);
        transfer.setCreatedAt(LocalDateTime.now());

        String message;

        if (amount.compareTo(VERIFICATION_THRESHOLD) > 0) {
            // Rule 6: large amount needs additional verification, balances stay unchanged
            transfer.setStatus(TransferStatus.PENDING_VERIFICATION);
            message = "Transfer requires additional verification. Balances were not changed";
        } else {
            // Rule 7: successful transfer updates both accounts
            from.setBalance(from.getBalance().subtract(amount));
            to.setBalance(to.getBalance().add(amount));
            accountRepository.save(from);
            accountRepository.save(to);
            transfer.setStatus(TransferStatus.SUCCESS);
            message = "Transfer completed successfully";
        }

        Transfer saved = transferRepository.save(transfer);
        return toResponse(saved, message);
    }

    private TransferResponse toResponse(Transfer transfer, String message) {
        TransferResponse response = new TransferResponse();
        response.setTransferId(transfer.getId());
        response.setFromAccountNumber(transfer.getFromAccount().getAccountNumber());
        response.setToAccountNumber(transfer.getToAccount().getAccountNumber());
        response.setAmount(transfer.getAmount());
        response.setStatus(transfer.getStatus());
        response.setMessage(message);
        response.setCreatedAt(transfer.getCreatedAt());
        return response;
    }
}
