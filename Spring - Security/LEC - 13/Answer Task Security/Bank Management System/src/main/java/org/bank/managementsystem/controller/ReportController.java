package org.bank.managementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.bank.managementsystem.dto.ReportSummaryDto;
import org.bank.managementsystem.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/summary")
    public ResponseEntity<ReportSummaryDto> summary() {
        return ResponseEntity.ok(reportService.summary());
    }
}