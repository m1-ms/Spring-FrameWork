package org.bank.managementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bank.managementsystem.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeResponse {

    private Long id;
    private String fullName;
    private String email;
    private Set<String> roles;
    private Set<String> authorities;

    public static MeResponse from(CustomUserDetails user) {
        return MeResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(new TreeSet<>(user.getRoles()))
                .authorities(user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toCollection(TreeSet::new)))
                .build();
    }
}