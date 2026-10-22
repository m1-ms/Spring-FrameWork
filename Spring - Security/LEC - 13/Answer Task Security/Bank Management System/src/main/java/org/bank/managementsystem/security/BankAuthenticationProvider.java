package org.bank.managementsystem.security;

import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.entity.Customer;
import org.bank.managementsystem.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankAuthenticationProvider implements AuthenticationProvider {

    private static final String INVALID_CREDENTIALS = "Invalid email or password";

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName() == null
                ? ""
                : authentication.getName().trim().toLowerCase();

        Object credentials = authentication.getCredentials();
        String rawPassword = (credentials == null) ? "" : credentials.toString();

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException(INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(rawPassword, customer.getPassword())) {
            throw new BadCredentialsException(INVALID_CREDENTIALS);
        }

        if (!customer.isEnabled()) {
            throw new DisabledException("Customer account is disabled");
        }

        if (customer.isBlocked()) {
            throw new LockedException("Customer account is blocked");
        }

        if (customer.getRoles().isEmpty()) {
            throw new InsufficientAuthenticationException("Customer has no valid role");
        }

        CustomUserDetails principal = new CustomUserDetails(customer);

        return UsernamePasswordAuthenticationToken.authenticated(
                principal, null, principal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}