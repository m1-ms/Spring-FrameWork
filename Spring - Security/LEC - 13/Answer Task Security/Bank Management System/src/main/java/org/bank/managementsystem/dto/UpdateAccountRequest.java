package org.bank.managementsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bank.managementsystem.enums.AccountStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {

    @NotNull(message = "Status is required")
    private AccountStatus status;
}