package org.bank.managementsystem.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Za-z]{3}$", message = "Currency must be a 3-letter code")
    private String currency;

    @PositiveOrZero(message = "Initial balance cannot be negative")
    @Digits(integer = 17, fraction = 2, message = "Initial balance allows at most 2 decimal places")
    private BigDecimal initialBalance;
}