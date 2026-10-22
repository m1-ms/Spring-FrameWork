package org.bank.managementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleRequestDto {

    @NotBlank(message = "Role name is required")
    @Pattern(regexp = "^[A-Z][A-Z0-9_]{2,49}$",
            message = "Role name must be 3-50 characters: uppercase letters, digits and underscores")
    private String name;

    @NotNull(message = "Permissions are required (the list may be empty)")
    private Set<@NotBlank(message = "Permission name must not be blank") String> permissions;
}