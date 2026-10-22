package org.bank.managementsystem.security;

import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.entity.Customer;
import org.bank.managementsystem.repository.CustomerRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String normalizedEmail = (email == null) ? "" : email.trim().toLowerCase();

        Customer customer = customerRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));

        if (!customer.isEnabled()) {
            throw new DisabledException("Customer account is disabled");
        }

        if (customer.isBlocked()) {
            throw new LockedException("Customer account is blocked");
        }

        return new CustomUserDetails(customer);
    }
}