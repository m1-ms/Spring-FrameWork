package org.example.employee.email.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.employee.email.dto.EmailDTO;
import org.example.employee.email.service.EmailService;
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
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<EmailDTO> create(@Valid @RequestBody EmailDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmailDTO> update(@PathVariable("id") Long id,
                                           @Valid @RequestBody EmailDTO dto) {
        return ResponseEntity.ok(emailService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        emailService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EmailDTO>> getAll() {
        return ResponseEntity.ok(emailService.getAll());
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<EmailDTO>> getByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(emailService.getByName(name));
    }

    @GetMapping("/by-names")
    public ResponseEntity<List<EmailDTO>> getByNames(@RequestParam("names") List<String> names) {
        return ResponseEntity.ok(emailService.getByNames(names));
    }

    @GetMapping("/by-content")
    public ResponseEntity<List<EmailDTO>> getByContent(@RequestParam("content") String content) {
        return ResponseEntity.ok(emailService.getByContent(content));
    }
}