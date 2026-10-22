package org.bank.managementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportSummaryDto {

    private long totalCustomers;
    private long totalAccounts;
    private Map<String, Long> accountsByStatus;
    private long totalTransfers;
    private Map<String, Long> transfersByStatus;
    private Map<String, BigDecimal> totalBalanceByCurrency;
}