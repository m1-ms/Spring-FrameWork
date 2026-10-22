package org.bank.managementsystem.service;

import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.dto.AssignRolesRequestDto;
import org.bank.managementsystem.dto.CreateCustomerRequestDto;
import org.bank.managementsystem.dto.CustomerDto;
import org.bank.managementsystem.entity.Customer;
import org.bank.managementsystem.entity.Role;
import org.bank.managementsystem.mapper.DtoMapper;
import org.bank.managementsystem.repository.CustomerRepository;
import org.bank.managementsystem.repository.RoleRepository;
import org.bank.managementsystem.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class AdminCustomerService {

    private static final String ADMIN_ROLE = "ADMIN";

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DtoMapper mapper;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CustomerDto create(CreateCustomerRequestDto request) {
        String email = request.getEmail().trim().toLowerCase();

        if (customerRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }

        Customer customer = Customer.builder()
                .fullName(request.getFullName().trim())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .build();

        customer.getRoles().addAll(resolveRoles(request.getRoles()));

        return mapper.toCustomerDto(customerRepository.save(customer));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CustomerDto assignRoles(Long id, AssignRolesRequestDto request, CustomUserDetails admin) {
        Set<Role> newRoles = resolveRoles(request.getRoles());

        boolean keepsAdmin = newRoles.stream().anyMatch(role -> ADMIN_ROLE.equals(role.getName()));
        if (id.equals(admin.getId()) && !keepsAdmin) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "You cannot remove your own ADMIN role");
        }

        Customer customer = findOrThrow(id);
        customer.getRoles().clear();
        customer.getRoles().addAll(newRoles);

        return mapper.toCustomerDto(customerRepository.save(customer));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CustomerDto block(Long id, CustomUserDetails admin) {
        return apply(id, admin, true, customer -> customer.setBlocked(true));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CustomerDto unblock(Long id, CustomUserDetails admin) {
        return apply(id, admin, false, customer -> customer.setBlocked(false));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CustomerDto disable(Long id, CustomUserDetails admin) {
        return apply(id, admin, true, customer -> customer.setEnabled(false));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CustomerDto enable(Long id, CustomUserDetails admin) {
        return apply(id, admin, false, customer -> customer.setEnabled(true));
    }

    private CustomerDto apply(Long id, CustomUserDetails admin, boolean locksOut,
                              Consumer<Customer> change) {
        if (locksOut && id.equals(admin.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "You cannot lock out your own account");
        }

        Customer customer = findOrThrow(id);
        change.accept(customer);

        return mapper.toCustomerDto(customerRepository.save(customer));
    }

    private Set<Role> resolveRoles(Set<String> names) {
        Set<Role> roles = new HashSet<>();
        for (String raw : names) {
            String name = raw.trim().toUpperCase();
            Role role = roleRepository.findByName(name)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Unknown role: " + name));
            roles.add(role);
        }
        return roles;
    }

    private Customer findOrThrow(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }
}