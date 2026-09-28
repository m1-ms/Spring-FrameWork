package org.example.employee.email.mapper;

import org.example.employee.email.dto.EmailDTO;
import org.example.employee.email.entity.Email;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {

    public EmailDTO toDTO(Email email) {
        EmailDTO dto = new EmailDTO();
        dto.setId(email.getId());
        dto.setName(email.getName());
        dto.setContent(email.getContent());
        if (email.getEmployee() != null) {
            dto.setEmployeeId(email.getEmployee().getId());
        }
        return dto;
    }

    public Email toEntity(EmailDTO dto) {
        Email email = new Email();
        email.setName(dto.getName());
        email.setContent(dto.getContent());
        return email;
    }

    public void updateEntity(EmailDTO dto, Email email) {
        email.setName(dto.getName());
        email.setContent(dto.getContent());
    }
}