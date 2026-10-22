package org.bank.managementsystem.security;

import org.bank.managementsystem.service.AdminCatalogService;
import org.bank.managementsystem.service.AdminCustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AdminSecurityTest {

    private static final Long MISSING_ID = 999_999L;

    @Autowired
    private AdminCatalogService adminCatalogService;

    @Autowired
    private AdminCustomerService adminCustomerService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminRoleCanListRoles() {
        assertNotNull(adminCatalogService.listRoles());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminRolePassesTheSecurityLayerOnCustomerManagement() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> adminCustomerService.unblock(MISSING_ID, null));
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    @WithMockUser(roles = "BANK_MANAGER")
    void bankManagerRoleCannotListRoles() {
        assertThrows(AccessDeniedException.class, () -> adminCatalogService.listRoles());
    }

    @Test
    @WithMockUser(roles = "AUDITOR")
    void auditorRoleCannotCreatePermissions() {
        assertThrows(AccessDeniedException.class, () -> adminCatalogService.createPermission(null));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void customerRoleCannotCreateRoles() {
        assertThrows(AccessDeniedException.class, () -> adminCatalogService.createRole(null));
    }

    @Test
    @WithMockUser(authorities = {"ACCOUNT_READ", "ACCOUNT_CREATE", "ACCOUNT_UPDATE", "ACCOUNT_DELETE",
            "TRANSFER_CREATE", "TRANSFER_READ", "TRANSFER_APPROVE",
            "CUSTOMER_READ", "CUSTOMER_UPDATE", "REPORT_VIEW"})
    void allTenPermissionsWithoutTheAdminRoleOpenNothingInAdminApis() {
        assertThrows(AccessDeniedException.class, () -> adminCustomerService.unblock(MISSING_ID, null));
        assertThrows(AccessDeniedException.class, () -> adminCatalogService.listPermissions());
    }
}