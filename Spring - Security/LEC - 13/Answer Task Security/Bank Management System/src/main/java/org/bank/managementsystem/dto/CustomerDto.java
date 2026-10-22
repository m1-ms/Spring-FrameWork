package org.bank.managementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private boolean enabled;
    private boolean blocked;
    private LocalDateTime createdAt;
    private Set<String> roles;
}