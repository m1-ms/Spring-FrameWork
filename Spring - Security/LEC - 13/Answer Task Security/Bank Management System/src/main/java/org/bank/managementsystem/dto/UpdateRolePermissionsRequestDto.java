package org.bank.managementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRolePermissionsRequestDto {

    @NotNull(message = "Permissions are required (the list may be empty)")
    private Set<@NotBlank(message = "Permission name must not be blank") String> permissions;
}