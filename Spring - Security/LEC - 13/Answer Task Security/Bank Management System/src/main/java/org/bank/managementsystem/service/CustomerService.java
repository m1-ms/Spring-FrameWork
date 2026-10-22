package org.bank.managementsystem.service;

import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.dto.CustomerDto;
import org.bank.managementsystem.dto.UpdateCustomerRequest;
import org.bank.managementsystem.entity.Customer;
import org.bank.managementsystem.mapper.DtoMapper;
import org.bank.managementsystem.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final DtoMapper mapper;

    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @Transactional(readOnly = true)
    public CustomerDto getById(Long id) {
        return mapper.toCustomerDto(findOrThrow(id));
    }

    @PreAuthorize("hasAuthority('CUSTOMER_UPDATE')")
    @Transactional
    public CustomerDto update(Long id, UpdateCustomerRequest request) {
        Customer customer = findOrThrow(id);
        customer.setFullName(request.getFullName().trim());
        customer.setPhone(request.getPhone());
        return mapper.toCustomerDto(customerRepository.save(customer));
    }

    private Customer findOrThrow(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }
}