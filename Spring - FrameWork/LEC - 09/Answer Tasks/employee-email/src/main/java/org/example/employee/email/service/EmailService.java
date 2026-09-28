package org.example.employee.email.service;

import lombok.RequiredArgsConstructor;
import org.example.employee.email.dto.EmailDTO;
import org.example.employee.email.entity.Email;
import org.example.employee.email.entity.Employee;
import org.example.employee.email.exception.ResourceNotFoundException;
import org.example.employee.email.mapper.EmailMapper;
import org.example.employee.email.repository.EmailRepository;
import org.example.employee.email.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final EmailRepository emailRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailMapper emailMapper;

    @Transactional
    public EmailDTO create(EmailDTO dto) {
        Email email = emailMapper.toEntity(dto);
        if (dto.getEmployeeId() != null) {
            email.setEmployee(findEmployeeById(dto.getEmployeeId()));
        }
        return emailMapper.toDTO(emailRepository.save(email));
    }

    @Transactional
    public EmailDTO update(Long id, EmailDTO dto) {
        Email email = findEntityById(id);
        emailMapper.updateEntity(dto, email);
        if (dto.getEmployeeId() != null) {
            email.setEmployee(findEmployeeById(dto.getEmployeeId()));
        }
        return emailMapper.toDTO(emailRepository.save(email));
    }

    @Transactional
    public void delete(Long id) {
        emailRepository.delete(findEntityById(id));
    }

    public List<EmailDTO> getAll() {
        return emailRepository.findAll().stream()
                .map(emailMapper::toDTO)
                .toList();
    }

    public List<EmailDTO> getByName(String name) {
        return emailRepository.findByName(name).stream()
                .map(emailMapper::toDTO)
                .toList();
    }

    public List<EmailDTO> getByNames(List<String> names) {
        return emailRepository.findByNameIn(names).stream()
                .map(emailMapper::toDTO)
                .toList();
    }

    public List<EmailDTO> getByContent(String content) {
        return emailRepository.findByContent(content).stream()
                .map(emailMapper::toDTO)
                .toList();
    }

    private Email findEntityById(Long id) {
        return emailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found with id: " + id));
    }

    private Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }
}