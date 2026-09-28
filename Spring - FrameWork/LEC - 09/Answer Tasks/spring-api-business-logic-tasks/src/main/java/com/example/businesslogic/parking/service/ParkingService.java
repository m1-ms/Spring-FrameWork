package com.example.businesslogic.parking.service;

import com.example.businesslogic.common.exception.BusinessException;
import com.example.businesslogic.common.exception.ResourceNotFoundException;
import com.example.businesslogic.parking.dto.CheckInRequest;
import com.example.businesslogic.parking.dto.TicketResponse;
import com.example.businesslogic.parking.entity.ParkingLot;
import com.example.businesslogic.parking.entity.ParkingTicket;
import com.example.businesslogic.parking.entity.Vehicle;
import com.example.businesslogic.parking.enums.TicketStatus;
import com.example.businesslogic.parking.repository.ParkingLotRepository;
import com.example.businesslogic.parking.repository.ParkingTicketRepository;
import com.example.businesslogic.parking.repository.VehicleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ParkingService {

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    // Rule 5: the discount for VIP vehicles
    private static final BigDecimal VIP_DISCOUNT_PERCENT = new BigDecimal("20");

    // Rule 6: the fixed penalty for a lost ticket
    private static final BigDecimal LOST_TICKET_PENALTY = new BigDecimal("200");

    private final VehicleRepository vehicleRepository;
    private final ParkingLotRepository lotRepository;
    private final ParkingTicketRepository ticketRepository;

    public ParkingService(VehicleRepository vehicleRepository,
                          ParkingLotRepository lotRepository,
                          ParkingTicketRepository ticketRepository) {
        this.vehicleRepository = vehicleRepository;
        this.lotRepository = lotRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public TicketResponse checkIn(CheckInRequest request) {

        Vehicle vehicle = findVehicle(request.getPlateNumber());
        ParkingLot lot = lotRepository.findById(request.getLotId())
                .orElseThrow(() -> new ResourceNotFoundException("Parking lot not found"));

        // Rule 2: a vehicle with an active ticket cannot enter again
        if (ticketRepository.existsByVehicleIdAndStatus(vehicle.getId(), TicketStatus.ACTIVE)) {
            throw new BusinessException(
                    "Vehicle already has an active ticket", HttpStatus.CONFLICT);
        }

        // Rule 7: the parking capacity cannot be exceeded
        long occupied = ticketRepository.countByParkingLotIdAndStatus(lot.getId(), TicketStatus.ACTIVE);
        if (occupied >= lot.getCapacity()) {
            throw new BusinessException("Parking lot is full", HttpStatus.CONFLICT);
        }

        // Rule 1: the vehicle receives a parking ticket when entering
        ParkingTicket ticket = new ParkingTicket();
        ticket.setVehicle(vehicle);
        ticket.setParkingLot(lot);
        ticket.setEntryTime(LocalDateTime.now());
        ticket.setStatus(TicketStatus.ACTIVE);

        ParkingTicket saved = ticketRepository.save(ticket);
        return toResponse(saved, null, "Ticket issued. Welcome");
    }

    @Transactional
    public TicketResponse checkOut(Long ticketId) {

        ParkingTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        if (ticket.getStatus() != TicketStatus.ACTIVE) {
            throw new BusinessException("Ticket is not active", HttpStatus.CONFLICT);
        }

        LocalDateTime exitTime = LocalDateTime.now();

        // Any started hour is billed as a full hour, with a minimum of one hour
        long minutes = Duration.between(ticket.getEntryTime(), exitTime).toMinutes();
        int billedHours = (int) Math.max(1, (minutes + 59) / 60);

        ticket.setExitTime(exitTime);
        ticket.setFee(calculateFee(ticket.getVehicle(), billedHours));
        ticket.setStatus(TicketStatus.PAID);

        ParkingTicket saved = ticketRepository.save(ticket);
        return toResponse(saved, billedHours, "Payment completed. Have a safe trip");
    }

    // Rule 6: a lost ticket has a fixed penalty
    @Transactional
    public TicketResponse reportLostTicket(String plateNumber) {

        Vehicle vehicle = findVehicle(plateNumber);

        ParkingTicket ticket = ticketRepository
                .findByVehicleIdAndStatus(vehicle.getId(), TicketStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle has no active ticket"));

        ticket.setExitTime(LocalDateTime.now());
        ticket.setFee(LOST_TICKET_PENALTY);
        ticket.setStatus(TicketStatus.LOST);

        ParkingTicket saved = ticketRepository.save(ticket);
        return toResponse(saved, null, "Ticket reported as lost. The fixed penalty was applied");
    }

    // Rule 3, 4 and 5: fee by vehicle type, first hour fixed, additional hours, VIP discount
    private BigDecimal calculateFee(Vehicle vehicle, int billedHours) {
        BigDecimal firstHour = vehicle.getType().getFirstHourFee();
        BigDecimal additionalHours = vehicle.getType().getAdditionalHourFee()
                .multiply(BigDecimal.valueOf(billedHours - 1));

        BigDecimal total = firstHour.add(additionalHours);

        if (vehicle.isVip()) {
            BigDecimal discount = total.multiply(VIP_DISCOUNT_PERCENT)
                    .divide(HUNDRED, 2, RoundingMode.HALF_UP);
            total = total.subtract(discount);
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private Vehicle findVehicle(String plateNumber) {
        String normalized = plateNumber.trim().toUpperCase();
        return vehicleRepository.findByPlateNumber(normalized)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    private TicketResponse toResponse(ParkingTicket ticket, Integer billedHours, String message) {
        TicketResponse response = new TicketResponse();
        response.setTicketId(ticket.getId());
        response.setPlateNumber(ticket.getVehicle().getPlateNumber());
        response.setVehicleType(ticket.getVehicle().getType());
        response.setVip(ticket.getVehicle().isVip());
        response.setLotId(ticket.getParkingLot().getId());
        response.setEntryTime(ticket.getEntryTime());
        response.setExitTime(ticket.getExitTime());
        response.setBilledHours(billedHours);
        response.setFee(ticket.getFee());
        response.setStatus(ticket.getStatus());
        response.setMessage(message);
        return response;
    }
}
