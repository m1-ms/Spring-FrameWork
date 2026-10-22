package org.bank.managementsystem.security;

import org.bank.managementsystem.service.AccountService;
import org.bank.managementsystem.service.CustomerService;
import org.bank.managementsystem.service.TransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MethodSecurityTest {

    private static final Long MISSING_ID = 999_999L;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TransferService transferService;


    @Test
    @WithMockUser(authorities = "ACCOUNT_READ")
    void createAccountNeedsAccountCreate() {
        assertThrows(AccessDeniedException.class, () -> accountService.create(null));
    }

    @Test
    @WithMockUser(authorities = "ACCOUNT_READ")
    void updateAccountNeedsAccountUpdate() {
        assertThrows(AccessDeniedException.class, () -> accountService.updateStatus(MISSING_ID, null));
    }

    @Test
    @WithMockUser(authorities = "ACCOUNT_READ")
    void deleteAccountNeedsAccountDelete() {
        assertThrows(AccessDeniedException.class, () -> accountService.delete(MISSING_ID));
    }

    @Test
    @WithMockUser(authorities = "TRANSFER_READ")
    void readAccountNeedsAccountRead() {
        assertThrows(AccessDeniedException.class, () -> accountService.getById(MISSING_ID, null));
    }

    @Test
    @WithMockUser(authorities = "ACCOUNT_READ")
    void readCustomerNeedsCustomerRead() {
        assertThrows(AccessDeniedException.class, () -> customerService.getById(MISSING_ID));
    }

    @Test
    @WithMockUser(authorities = "CUSTOMER_READ")
    void updateCustomerNeedsCustomerUpdate() {
        assertThrows(AccessDeniedException.class, () -> customerService.update(MISSING_ID, null));
    }

    @Test
    @WithMockUser(authorities = "TRANSFER_READ")
    void createTransferNeedsTransferCreate() {
        assertThrows(AccessDeniedException.class, () -> transferService.create(null, null));
    }

    @Test
    @WithMockUser(authorities = "TRANSFER_CREATE")
    void readTransferNeedsTransferRead() {
        assertThrows(AccessDeniedException.class, () -> transferService.getById(MISSING_ID, null));
    }

    @Test
    @WithMockUser(authorities = {"TRANSFER_CREATE", "TRANSFER_READ"})
    void customerCannotApproveTransfer() {
        assertThrows(AccessDeniedException.class, () -> transferService.approve(MISSING_ID, null));
    }

    @Test
    @WithMockUser(authorities = {"ACCOUNT_READ", "TRANSFER_READ", "REPORT_VIEW"})
    void auditorCannotApproveTransfer() {
        assertThrows(AccessDeniedException.class, () -> transferService.approve(MISSING_ID, null));
    }

    @Test
    @WithAnonymousUser
    void anonymousCannotDeleteAccount() {
        assertThrows(AccessDeniedException.class, () -> accountService.delete(MISSING_ID));
    }


    @Test
    @WithMockUser(authorities = "ACCOUNT_DELETE")
    void deleteAccountPassesWithAccountDelete() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> accountService.delete(MISSING_ID));
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    @WithMockUser(authorities = "CUSTOMER_READ")
    void readCustomerPassesWithCustomerRead() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> customerService.getById(MISSING_ID));
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    @WithMockUser(authorities = "TRANSFER_APPROVE")
    void approveTransferPassesWithTransferApprove() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> transferService.approve(MISSING_ID, null));
        assertEquals(404, ex.getStatusCode().value());
    }
}