package org.example.employee.email.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {

    private Long id;

    @NotBlank(message = "Email name must not be null or empty")
    private String name;

    @NotBlank(message = "Email content must not be null or empty")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email content must match the email pattern"
    )
    private String content;

    private Long employeeId;
}