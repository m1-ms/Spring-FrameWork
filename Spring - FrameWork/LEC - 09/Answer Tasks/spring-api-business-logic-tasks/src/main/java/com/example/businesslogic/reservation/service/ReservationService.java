package com.example.businesslogic.reservation.service;

import com.example.businesslogic.common.exception.BusinessException;
import com.example.businesslogic.common.exception.ResourceNotFoundException;
import com.example.businesslogic.reservation.dto.ReservationRequest;
import com.example.businesslogic.reservation.dto.ReservationResponse;
import com.example.businesslogic.reservation.entity.Reservation;
import com.example.businesslogic.reservation.entity.RestaurantCustomer;
import com.example.businesslogic.reservation.entity.RestaurantTable;
import com.example.businesslogic.reservation.enums.ReservationStatus;
import com.example.businesslogic.reservation.repository.ReservationRepository;
import com.example.businesslogic.reservation.repository.RestaurantCustomerRepository;
import com.example.businesslogic.reservation.repository.RestaurantTableRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    private static final LocalTime OPENING_TIME = LocalTime.of(10, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(23, 0);
    private static final int RESERVATION_DURATION_MINUTES = 90;
    private static final int MAX_ACTIVE_RESERVATIONS = 2;
    private static final int MIN_ADVANCE_MINUTES = 30;
    private static final int CANCELLATION_HOURS = 1;
    private static final int CONFIRMATION_MINUTES = 15;

    private static final List<ReservationStatus> ACTIVE_STATUSES =
            List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED);

    private final RestaurantTableRepository tableRepository;
    private final RestaurantCustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(RestaurantTableRepository tableRepository,
                              RestaurantCustomerRepository customerRepository,
                              ReservationRepository reservationRepository) {
        this.tableRepository = tableRepository;
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
    }

    // Rule 7: a reservation that is not confirmed within 15 minutes expires.
    // Runs every minute, and also before every operation below.
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void expirePendingReservations() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(CONFIRMATION_MINUTES);
        reservationRepository.expireStale(
                ReservationStatus.EXPIRED, ReservationStatus.PENDING, threshold);
    }

    @Transactional
    public ReservationResponse create(ReservationRequest request) {

        expirePendingReservations();

        RestaurantTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
        RestaurantCustomer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Rule 2: the guests must fit the table capacity
        if (request.getGuests() > table.getCapacity()) {
            throw new BusinessException("Number of guests exceeds the table capacity");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = request.getStartTime();
        LocalDateTime end = start.plusMinutes(RESERVATION_DURATION_MINUTES);

        // Rule 5: the reservation must be made at least 30 minutes in advance
        if (start.isBefore(now.plusMinutes(MIN_ADVANCE_MINUTES))) {
            throw new BusinessException("Reservation must be made at least "
                    + MIN_ADVANCE_MINUTES + " minutes in advance");
        }

        // Rule 3: only during the restaurant working hours
        boolean sameDay = start.toLocalDate().equals(end.toLocalDate());
        if (!sameDay
                || start.toLocalTime().isBefore(OPENING_TIME)
                || end.toLocalTime().isAfter(CLOSING_TIME)) {
            throw new BusinessException("Reservations are available only between "
                    + OPENING_TIME + " and " + CLOSING_TIME
                    + " (each reservation lasts " + RESERVATION_DURATION_MINUTES + " minutes)");
        }

        // Rule 4: a customer cannot have more than 2 active reservations
        long activeCount = reservationRepository.countActiveByCustomer(
                customer.getId(), ACTIVE_STATUSES, now);
        if (activeCount >= MAX_ACTIVE_RESERVATIONS) {
            throw new BusinessException("Customer cannot have more than "
                    + MAX_ACTIVE_RESERVATIONS + " active reservations", HttpStatus.CONFLICT);
        }

        // Rule 1: a table cannot have overlapping reservations
        long overlapping = reservationRepository.countOverlapping(
                table.getId(), ACTIVE_STATUSES, start, end);
        if (overlapping > 0) {
            throw new BusinessException(
                    "Table is already reserved at this time", HttpStatus.CONFLICT);
        }

        Reservation reservation = new Reservation();
        reservation.setRestaurantTable(table);
        reservation.setCustomer(customer);
        reservation.setGuests(request.getGuests());
        reservation.setStartTime(start);
        reservation.setEndTime(end);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(now);

        Reservation saved = reservationRepository.save(reservation);
        return toResponse(saved, "Reservation created. Confirm it within "
                + CONFIRMATION_MINUTES + " minutes or it will expire");
    }

    @Transactional
    public ReservationResponse confirm(Long reservationId) {

        expirePendingReservations();
        Reservation reservation = findReservation(reservationId);

        if (reservation.getStatus() == ReservationStatus.EXPIRED) {
            throw new BusinessException(
                    "Reservation has expired because it was not confirmed in time",
                    HttpStatus.CONFLICT);
        }
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new BusinessException(
                    "Only a pending reservation can be confirmed", HttpStatus.CONFLICT);
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        Reservation saved = reservationRepository.save(reservation);
        return toResponse(saved, "Reservation confirmed");
    }

    @Transactional
    public ReservationResponse cancel(Long reservationId) {

        expirePendingReservations();
        Reservation reservation = findReservation(reservationId);

        if (!reservation.getStatus().isActive()) {
            throw new BusinessException(
                    "Only an active reservation can be canceled", HttpStatus.CONFLICT);
        }

        // Rule 6: cancellation is allowed only before 1 hour of the reservation
        LocalDateTime lastCancelTime = reservation.getStartTime().minusHours(CANCELLATION_HOURS);
        if (LocalDateTime.now().isAfter(lastCancelTime)) {
            throw new BusinessException("Cancellation is allowed only before "
                    + CANCELLATION_HOURS + " hour of the reservation", HttpStatus.CONFLICT);
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        Reservation saved = reservationRepository.save(reservation);
        return toResponse(saved, "Reservation canceled");
    }

    private Reservation findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
    }

    private ReservationResponse toResponse(Reservation reservation, String message) {
        ReservationResponse response = new ReservationResponse();
        response.setReservationId(reservation.getId());
        response.setTableId(reservation.getRestaurantTable().getId());
        response.setTableNumber(reservation.getRestaurantTable().getTableNumber());
        response.setCustomerId(reservation.getCustomer().getId());
        response.setCustomerName(reservation.getCustomer().getName());
        response.setGuests(reservation.getGuests());
        response.setStartTime(reservation.getStartTime());
        response.setEndTime(reservation.getEndTime());
        response.setStatus(reservation.getStatus());
        response.setMessage(message);
        return response;
    }
}
