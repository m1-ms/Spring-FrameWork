package org.example.employee.email.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.employee.email.dto.EmployeeDTO;
import org.example.employee.email.dto.EmployeeWithEmailsDTO;
import org.example.employee.email.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDTO> create(@Valid @RequestBody EmployeeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(@PathVariable("id") Long id,
                                              @Valid @RequestBody EmployeeDTO dto) {
        return ResponseEntity.ok(employeeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAll() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<EmployeeDTO>> getByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(employeeService.getByIds(ids));
    }

    @GetMapping("/by-names")
    public ResponseEntity<List<EmployeeDTO>> getByNames(@RequestParam("names") List<String> names) {
        return ResponseEntity.ok(employeeService.getByNames(names));
    }

    @PostMapping("/with-emails")
    public ResponseEntity<EmployeeWithEmailsDTO> createWithEmails(
            @Valid @RequestBody EmployeeWithEmailsDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createWithEmails(dto));
    }
}