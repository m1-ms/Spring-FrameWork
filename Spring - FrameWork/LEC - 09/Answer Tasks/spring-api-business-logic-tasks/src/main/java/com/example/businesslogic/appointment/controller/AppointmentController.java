package com.example.businesslogic.appointment.controller;

import com.example.businesslogic.appointment.dto.AppointmentResponse;
import com.example.businesslogic.appointment.dto.BookAppointmentRequest;
import com.example.businesslogic.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> book(@Valid @RequestBody BookAppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.book(request));
    }

    @PostMapping("/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponse> cancel(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentService.cancel(appointmentId));
    }

    @PostMapping("/{appointmentId}/rebook")
    public ResponseEntity<AppointmentResponse> rebook(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentService.rebook(appointmentId));
    }
}
