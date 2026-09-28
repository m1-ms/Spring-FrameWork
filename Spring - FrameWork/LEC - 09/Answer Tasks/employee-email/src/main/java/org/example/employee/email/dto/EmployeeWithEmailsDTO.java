package org.example.employee.email.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeWithEmailsDTO extends EmployeeDTO {

    @Valid
    @NotEmpty(message = "Emails list must not be empty")
    private List<EmailDTO> emails = new ArrayList<>();
}