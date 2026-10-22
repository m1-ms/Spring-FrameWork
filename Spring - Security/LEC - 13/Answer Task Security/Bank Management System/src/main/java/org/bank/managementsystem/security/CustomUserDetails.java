package org.bank.managementsystem.security;

import org.bank.managementsystem.entity.Customer;
import org.bank.managementsystem.entity.Permission;
import org.bank.managementsystem.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private static final String ROLE_PREFIX = "ROLE_";
    private static final Set<String> STAFF_ROLES = Set.of("ADMIN", "BANK_MANAGER", "AUDITOR");

    private final Long id;
    private final String fullName;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final boolean blocked;
    private final Set<String> roles;
    private final Set<GrantedAuthority> authorities;

    public CustomUserDetails(Customer customer) {
        this.id = customer.getId();
        this.fullName = customer.getFullName();
        this.email = customer.getEmail();
        this.password = customer.getPassword();
        this.enabled = customer.isEnabled();
        this.blocked = customer.isBlocked();

        this.roles = customer.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toUnmodifiableSet());

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : customer.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getName()));
            for (Permission permission : role.getPermissions()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        this.authorities = Collections.unmodifiableSet(grantedAuthorities);
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public boolean isStaff() {
        return roles.stream().anyMatch(STAFF_ROLES::contains);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !blocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}