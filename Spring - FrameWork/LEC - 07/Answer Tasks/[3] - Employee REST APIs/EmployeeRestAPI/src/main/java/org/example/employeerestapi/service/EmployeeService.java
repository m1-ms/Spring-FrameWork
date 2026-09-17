package org.example.employeerestapi.service;

import org.example.employeerestapi.entity.Employee;
import org.example.employeerestapi.exception.EmployeeNotFoundException;
import org.example.employeerestapi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getEmployeesByIds(List<Long> ids) {
        return employeeRepository.findByIdIn(ids);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> saveEmployees(List<Employee> employees) {
        return employeeRepository.saveAll(employees);
    }

    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> updateEmployees(List<Employee> employees) {
        return employeeRepository.saveAll(employees);
    }

    public void deleteAllEmployees() {
        employeeRepository.deleteAll();
    }

    public void deleteEmployeeById(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
    }

    public void deleteEmployeesByIds(List<Long> ids) {
        employeeRepository.deleteAllById(ids);
    }

    public List<Employee> searchByNameDerived(String name) {
        return employeeRepository.findByNameStartingWithIgnoreCase(name);
    }

    public List<Employee> searchByNameNative(String name) {
        return employeeRepository.searchByNameNative(name);
    }

    public List<Employee> searchByNameJPQL(String name) {
        return employeeRepository.searchByNameJPQL(name);
    }

    public Page<Employee> getEmployeesPaged(int page, int size) {
        return employeeRepository.findAll(PageRequest.of(page, size));
    }
}