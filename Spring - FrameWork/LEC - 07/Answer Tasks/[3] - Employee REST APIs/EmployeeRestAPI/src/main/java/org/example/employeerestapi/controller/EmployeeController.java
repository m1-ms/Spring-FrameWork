package org.example.employeerestapi.controller;

import jakarta.validation.Valid;
import org.example.employeerestapi.dto.PagedResponse;
import org.example.employeerestapi.entity.Employee;
import org.example.employeerestapi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // 1) Get All Employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // 2) Get Employees By List of IDs
    @GetMapping("/byIds")
    public ResponseEntity<List<Employee>> getEmployeesByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(employeeService.getEmployeesByIds(ids));
    }

    // 3) Save Employee
    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.saveEmployee(employee));
    }

    // 4) Save List of Employees
    @PostMapping("/batch")
    public ResponseEntity<List<Employee>> saveEmployees(@Valid @RequestBody List<Employee> employees) {
        return ResponseEntity.ok(employeeService.saveEmployees(employees));
    }

    // 5) Update Employee
    @PutMapping
    public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(employee));
    }

    // 6) Update List of Employees
    @PutMapping("/batch")
    public ResponseEntity<List<Employee>> updateEmployees(@Valid @RequestBody List<Employee> employees) {
        return ResponseEntity.ok(employeeService.updateEmployees(employees));
    }

    // 7) Delete All Employees
    @DeleteMapping
    public ResponseEntity<Void> deleteAllEmployees() {
        employeeService.deleteAllEmployees();
        return ResponseEntity.noContent().build();
    }

    // 8) Delete Employee By ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }

    // 9) Delete Employees By List of IDs
    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteEmployeesByIds(@RequestBody List<Long> ids) {
        employeeService.deleteEmployeesByIds(ids);
        return ResponseEntity.noContent().build();
    }

    // 10) Search Employee By Name

    @GetMapping("/search/derived")
    public ResponseEntity<List<Employee>> searchByNameDerived(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.searchByNameDerived(name));
    }

    @GetMapping("/search/native")
    public ResponseEntity<List<Employee>> searchByNameNative(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.searchByNameNative(name));
    }

    @GetMapping("/search/jpql")
    public ResponseEntity<List<Employee>> searchByNameJPQL(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.searchByNameJPQL(name));
    }

    @GetMapping("/paged")
    public ResponseEntity<PagedResponse<Employee>> getEmployeesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Employee> employeePage = employeeService.getEmployeesPaged(page, size);

        PagedResponse<Employee> response = new PagedResponse<>(
                employeePage.getContent(),
                employeePage.getNumber(),
                employeePage.getTotalPages(),
                employeePage.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

}