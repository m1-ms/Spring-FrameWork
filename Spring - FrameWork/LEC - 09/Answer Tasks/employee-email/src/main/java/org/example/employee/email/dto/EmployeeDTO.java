package org.example.employee.email.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "Name must not be null or empty")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 16, message = "Age must be greater than 15")
    @Max(value = 39, message = "Age must be less than 40")
    private Integer age;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "5000", inclusive = false, message = "Salary must be greater than 5000")
    @DecimalMax(value = "10000", inclusive = false, message = "Salary must be less than 10000")
    private Double salary;
}