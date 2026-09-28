package com.example.businesslogic.enrollment.controller;

import com.example.businesslogic.enrollment.dto.EnrollRequest;
import com.example.businesslogic.enrollment.dto.EnrollmentResponse;
import com.example.businesslogic.enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/enroll")
    public ResponseEntity<EnrollmentResponse> enroll(@Valid @RequestBody EnrollRequest request) {
        return ResponseEntity.ok(enrollmentService.enroll(request));
    }

    @PostMapping("/drop/{enrollmentId}")
    public ResponseEntity<EnrollmentResponse> drop(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.drop(enrollmentId));
    }
}
