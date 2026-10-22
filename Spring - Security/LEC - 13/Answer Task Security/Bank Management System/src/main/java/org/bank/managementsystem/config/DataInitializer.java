package org.bank.managementsystem.config;

import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.entity.Customer;
import org.bank.managementsystem.entity.Permission;
import org.bank.managementsystem.entity.Role;
import org.bank.managementsystem.repository.CustomerRepository;
import org.bank.managementsystem.repository.PermissionRepository;
import org.bank.managementsystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final String TEST_PASSWORD = "Pass12345";

    private static final List<String> PERMISSIONS = List.of(
            "ACCOUNT_READ",
            "ACCOUNT_CREATE",
            "ACCOUNT_UPDATE",
            "ACCOUNT_DELETE",
            "TRANSFER_CREATE",
            "TRANSFER_READ",
            "TRANSFER_APPROVE",
            "CUSTOMER_READ",
            "CUSTOMER_UPDATE",
            "REPORT_VIEW"
    );

    private static final Map<String, List<String>> ROLES_PERMISSIONS = new LinkedHashMap<>();

    // email > role
    private static final Map<String, String> TEST_USERS = new LinkedHashMap<>();

    static {
        ROLES_PERMISSIONS.put("ADMIN", List.of(
                "ACCOUNT_READ", "ACCOUNT_CREATE", "ACCOUNT_UPDATE", "ACCOUNT_DELETE",
                "TRANSFER_CREATE", "TRANSFER_READ", "TRANSFER_APPROVE",
                "CUSTOMER_READ", "CUSTOMER_UPDATE",
                "REPORT_VIEW"));

        ROLES_PERMISSIONS.put("BANK_MANAGER", List.of(
                "ACCOUNT_READ", "ACCOUNT_UPDATE",
                "TRANSFER_READ", "TRANSFER_APPROVE",
                "CUSTOMER_READ", "CUSTOMER_UPDATE"));

        ROLES_PERMISSIONS.put("CUSTOMER", List.of(
                "ACCOUNT_READ",
                "TRANSFER_CREATE", "TRANSFER_READ"));

        ROLES_PERMISSIONS.put("AUDITOR", List.of(
                "ACCOUNT_READ",
                "TRANSFER_READ",
                "REPORT_VIEW"));

        TEST_USERS.put("ahmed@test.com", "CUSTOMER");
        TEST_USERS.put("hacker@test.com", "CUSTOMER");
        TEST_USERS.put("third@test.com", "CUSTOMER");
        TEST_USERS.put("admin@test.com", "ADMIN");
        TEST_USERS.put("manager@test.com", "BANK_MANAGER");
        TEST_USERS.put("auditor@test.com", "AUDITOR");
    }

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed-test-users:false}")
    private boolean seedTestUsers;

    @Override
    public void run(String... args) {
        seedRolesAndPermissions();

        if (seedTestUsers) {
            seedTestUsers();
        }
    }

    private void seedRolesAndPermissions() {
        Map<String, Permission> savedPermissions = new HashMap<>();

        for (String name : PERMISSIONS) {
            Permission permission = permissionRepository.findByName(name)
                    .orElseGet(() -> permissionRepository.save(
                            Permission.builder().name(name).build()));
            savedPermissions.put(name, permission);
        }

        for (Map.Entry<String, List<String>> entry : ROLES_PERMISSIONS.entrySet()) {
            String roleName = entry.getKey();

            Role role = roleRepository.findByName(roleName)
                    .orElseGet(() -> Role.builder().name(roleName).build());

            Set<Permission> rolePermissions = new HashSet<>();
            for (String permissionName : entry.getValue()) {
                rolePermissions.add(savedPermissions.get(permissionName));
            }

            role.setPermissions(rolePermissions);
            roleRepository.save(role);
        }
    }

    private void seedTestUsers() {
        for (Map.Entry<String, String> entry : TEST_USERS.entrySet()) {
            String email = entry.getKey();

            Role role = roleRepository.findByName(entry.getValue())
                    .orElseThrow(() -> new IllegalStateException("Role is missing: " + entry.getValue()));

            Customer customer = customerRepository.findByEmail(email)
                    .orElseGet(() -> Customer.builder()
                            .fullName(displayName(email))
                            .email(email)
                            .phone("01000000000")
                            .build());

            customer.setPassword(passwordEncoder.encode(TEST_PASSWORD));
            customer.setEnabled(true);
            customer.setBlocked(false);
            customer.getRoles().clear();
            customer.getRoles().add(role);

            customerRepository.save(customer);
        }
    }

    private String displayName(String email) {
        String prefix = email.substring(0, email.indexOf('@'));
        return Character.toUpperCase(prefix.charAt(0)) + prefix.substring(1) + " User";
    }
}