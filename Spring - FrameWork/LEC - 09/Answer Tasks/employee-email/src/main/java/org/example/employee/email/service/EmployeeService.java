package org.example.employee.email.service;

import lombok.RequiredArgsConstructor;
import org.example.employee.email.dto.EmployeeDTO;
import org.example.employee.email.dto.EmployeeWithEmailsDTO;
import org.example.employee.email.entity.Employee;
import org.example.employee.email.exception.ResourceNotFoundException;
import org.example.employee.email.mapper.EmployeeMapper;
import org.example.employee.email.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Transactional
    public EmployeeDTO create(EmployeeDTO dto) {
        Employee saved = employeeRepository.save(employeeMapper.toEntity(dto));
        return employeeMapper.toDTO(saved);
    }

    @Transactional
    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        Employee employee = findEntityById(id);
        employeeMapper.updateEntity(dto, employee);
        return employeeMapper.toDTO(employeeRepository.save(employee));
    }

    @Transactional
    public void delete(Long id) {
        employeeRepository.delete(findEntityById(id));
    }

    public List<EmployeeDTO> getAll() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    public EmployeeDTO getById(Long id) {
        return employeeMapper.toDTO(findEntityById(id));
    }

    public List<EmployeeDTO> getByIds(List<Long> ids) {
        return employeeRepository.findAllById(ids).stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    public List<EmployeeDTO> getByNames(List<String> names) {
        return employeeRepository.findByNameIn(names).stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    @Transactional
    public EmployeeWithEmailsDTO createWithEmails(EmployeeWithEmailsDTO dto) {
        Employee saved = employeeRepository.save(employeeMapper.toEntity(dto));
        return employeeMapper.toWithEmailsDTO(saved);
    }

    private Employee findEntityById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }
}