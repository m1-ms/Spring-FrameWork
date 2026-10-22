package org.bank.managementsystem.service;

import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.dto.CustomerDto;
import org.bank.managementsystem.dto.RegisterRequestDto;
import org.bank.managementsystem.entity.Customer;
import org.bank.managementsystem.entity.Role;
import org.bank.managementsystem.mapper.DtoMapper;
import org.bank.managementsystem.repository.CustomerRepository;
import org.bank.managementsystem.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String DEFAULT_ROLE = "CUSTOMER";

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DtoMapper mapper;

    @Transactional
    public CustomerDto register(RegisterRequestDto request) {
        if (!request.getUnknownFields().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Request contains unsupported fields");
        }

        String email = request.getEmail().trim().toLowerCase();

        if (customerRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }

        Role customerRole = roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new IllegalStateException("Default role CUSTOMER is missing"));

        Customer customer = Customer.builder()
                .fullName(request.getFullName().trim())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .build();

        customer.getRoles().add(customerRole);

        return mapper.toCustomerDto(customerRepository.save(customer));
    }
}