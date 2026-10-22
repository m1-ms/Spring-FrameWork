package org.bank.managementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePermissionRequestDto {

    @NotBlank(message = "Permission name is required")
    @Pattern(regexp = "^[A-Z][A-Z0-9_]{2,49}$",
            message = "Permission name must be 3-50 characters: uppercase letters, digits and underscores")
    private String name;
}