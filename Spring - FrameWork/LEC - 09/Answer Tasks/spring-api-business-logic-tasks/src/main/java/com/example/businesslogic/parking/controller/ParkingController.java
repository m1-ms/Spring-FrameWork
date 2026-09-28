package com.example.businesslogic.parking.controller;

import com.example.businesslogic.parking.dto.CheckInRequest;
import com.example.businesslogic.parking.dto.TicketResponse;
import com.example.businesslogic.parking.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/entry")
    public ResponseEntity<TicketResponse> checkIn(@Valid @RequestBody CheckInRequest request) {
        return ResponseEntity.ok(parkingService.checkIn(request));
    }

    @PostMapping("/exit/{ticketId}")
    public ResponseEntity<TicketResponse> checkOut(@PathVariable Long ticketId) {
        return ResponseEntity.ok(parkingService.checkOut(ticketId));
    }

    @PostMapping("/lost/{plateNumber}")
    public ResponseEntity<TicketResponse> reportLost(@PathVariable String plateNumber) {
        return ResponseEntity.ok(parkingService.reportLostTicket(plateNumber));
    }
}
