package org.example.employee.email.mapper;

import lombok.RequiredArgsConstructor;
import org.example.employee.email.dto.EmployeeDTO;
import org.example.employee.email.dto.EmployeeWithEmailsDTO;
import org.example.employee.email.entity.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    private final ModelMapper modelMapper;
    private final EmailMapper emailMapper;

    public EmployeeDTO toDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public Employee toEntity(EmployeeDTO dto) {
        Employee employee = modelMapper.map(dto, Employee.class);
        employee.setId(null);
        return employee;
    }

    public void updateEntity(EmployeeDTO dto, Employee employee) {
        employee.setName(dto.getName());
        employee.setAge(dto.getAge());
        employee.setSalary(dto.getSalary());
    }

    public Employee toEntity(EmployeeWithEmailsDTO dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setAge(dto.getAge());
        employee.setSalary(dto.getSalary());
        dto.getEmails().forEach(emailDTO -> employee.addEmail(emailMapper.toEntity(emailDTO)));
        return employee;
    }

    public EmployeeWithEmailsDTO toWithEmailsDTO(Employee employee) {
        EmployeeWithEmailsDTO dto = new EmployeeWithEmailsDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setAge(employee.getAge());
        dto.setSalary(employee.getSalary());
        employee.getEmails().forEach(email -> dto.getEmails().add(emailMapper.toDTO(email)));
        return dto;
    }
}