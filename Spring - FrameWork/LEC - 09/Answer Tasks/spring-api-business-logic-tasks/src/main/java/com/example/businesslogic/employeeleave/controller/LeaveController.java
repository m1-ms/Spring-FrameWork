package com.example.businesslogic.employeeleave.controller;

import com.example.businesslogic.employeeleave.dto.CreateLeaveRequest;
import com.example.businesslogic.employeeleave.dto.LeaveResponse;
import com.example.businesslogic.employeeleave.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping
    public ResponseEntity<LeaveResponse> submit(@Valid @RequestBody CreateLeaveRequest request) {
        return ResponseEntity.ok(leaveService.submit(request));
    }

    @PostMapping("/{leaveRequestId}/approve")
    public ResponseEntity<LeaveResponse> approve(@PathVariable Long leaveRequestId) {
        return ResponseEntity.ok(leaveService.approve(leaveRequestId));
    }

    @PostMapping("/{leaveRequestId}/reject")
    public ResponseEntity<LeaveResponse> reject(@PathVariable Long leaveRequestId) {
        return ResponseEntity.ok(leaveService.reject(leaveRequestId));
    }

    @PostMapping("/{leaveRequestId}/cancel")
    public ResponseEntity<LeaveResponse> cancel(@PathVariable Long leaveRequestId) {
        return ResponseEntity.ok(leaveService.cancel(leaveRequestId));
    }
}
