package org.bank.managementsystem.security;

import org.bank.managementsystem.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ReportSecurityTest {

    @Autowired
    private ReportService reportService;

    @Test
    @WithMockUser(authorities = {"ACCOUNT_READ", "TRANSFER_CREATE", "TRANSFER_READ"})
    void customerCannotViewReports() {
        assertThrows(AccessDeniedException.class, () -> reportService.summary());
    }

    @Test
    @WithMockUser(authorities = {"ACCOUNT_READ", "ACCOUNT_UPDATE", "TRANSFER_READ",
            "TRANSFER_APPROVE", "CUSTOMER_READ", "CUSTOMER_UPDATE"})
    void bankManagerCannotViewReports() {
        assertThrows(AccessDeniedException.class, () -> reportService.summary());
    }

    @Test
    @WithMockUser(authorities = {"ACCOUNT_READ", "TRANSFER_READ", "REPORT_VIEW"})
    void auditorCanViewReports() {
        assertNotNull(reportService.summary());
    }
}