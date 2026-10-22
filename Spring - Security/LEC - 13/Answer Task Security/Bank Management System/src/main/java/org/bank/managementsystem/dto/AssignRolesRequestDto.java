package org.bank.managementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignRolesRequestDto {

    @NotEmpty(message = "At least one role is required")
    private Set<@NotBlank(message = "Role name must not be blank") String> roles;
}