package org.bank.managementsystem.service;

import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.dto.ReportSummaryDto;
import org.bank.managementsystem.enums.AccountStatus;
import org.bank.managementsystem.enums.TransferStatus;
import org.bank.managementsystem.repository.AccountRepository;
import org.bank.managementsystem.repository.CurrencyTotalView;
import org.bank.managementsystem.repository.CustomerRepository;
import org.bank.managementsystem.repository.TransferRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    @Transactional(readOnly = true)
    public ReportSummaryDto summary() {
        Map<String, Long> accountsByStatus = new LinkedHashMap<>();
        for (AccountStatus status : AccountStatus.values()) {
            accountsByStatus.put(status.name(), accountRepository.countByStatus(status));
        }

        Map<String, Long> transfersByStatus = new LinkedHashMap<>();
        for (TransferStatus status : TransferStatus.values()) {
            transfersByStatus.put(status.name(), transferRepository.countByStatus(status));
        }

        Map<String, BigDecimal> balanceByCurrency = new LinkedHashMap<>();
        for (CurrencyTotalView row : accountRepository.sumBalanceByCurrency()) {
            balanceByCurrency.put(row.getCurrency(), row.getTotal());
        }

        return ReportSummaryDto.builder()
                .totalCustomers(customerRepository.count())
                .totalAccounts(accountRepository.count())
                .accountsByStatus(accountsByStatus)
                .totalTransfers(transferRepository.count())
                .transfersByStatus(transfersByStatus)
                .totalBalanceByCurrency(balanceByCurrency)
                .build();
    }
}