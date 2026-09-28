package com.example.businesslogic.appointment.service;

import com.example.businesslogic.appointment.dto.AppointmentResponse;
import com.example.businesslogic.appointment.dto.BookAppointmentRequest;
import com.example.businesslogic.appointment.entity.Appointment;
import com.example.businesslogic.appointment.entity.Doctor;
import com.example.businesslogic.appointment.entity.Patient;
import com.example.businesslogic.appointment.enums.AppointmentStatus;
import com.example.businesslogic.appointment.repository.AppointmentRepository;
import com.example.businesslogic.appointment.repository.DoctorRepository;
import com.example.businesslogic.appointment.repository.PatientRepository;
import com.example.businesslogic.common.exception.BusinessException;
import com.example.businesslogic.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class AppointmentService {

    private static final int MAX_APPOINTMENTS_PER_DAY = 2;
    private static final int CANCELLATION_HOURS = 2;

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(DoctorRepository doctorRepository,
                              PatientRepository patientRepository,
                              AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public AppointmentResponse book(BookAppointmentRequest request) {

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        // Rule 4: the doctor must be available
        if (!doctor.isAvailable()) {
            throw new BusinessException("Doctor is unavailable", HttpStatus.CONFLICT);
        }

        // Rule 3: the duration depends on the service type
        LocalDateTime start = request.getStartTime();
        LocalDateTime end = start.plusMinutes(request.getServiceType().getDurationMinutes());

        // Rule 4: the appointment must be inside the doctor's working hours
        LocalTime workStart = LocalTime.of(doctor.getWorkStartHour(), 0);
        LocalTime workEnd = LocalTime.of(doctor.getWorkEndHour(), 0);
        boolean sameDay = start.toLocalDate().equals(end.toLocalDate());
        if (!sameDay
                || start.toLocalTime().isBefore(workStart)
                || end.toLocalTime().isAfter(workEnd)) {
            throw new BusinessException(
                    "Appointment is outside the doctor's working hours", HttpStatus.CONFLICT);
        }

        // Rule 1: the doctor cannot have two appointments at the same time
        long overlapping = appointmentRepository.countOverlapping(
                doctor.getId(), AppointmentStatus.BOOKED, start, end);
        if (overlapping > 0) {
            throw new BusinessException(
                    "Doctor already has an appointment at this time", HttpStatus.CONFLICT);
        }

        // Rule 2: a patient cannot book more than 2 appointments on the same day
        LocalDateTime dayStart = start.toLocalDate().atStartOfDay();
        LocalDateTime dayEnd = dayStart.plusDays(1);
        long patientCount = appointmentRepository.countPatientAppointmentsBetween(
                patient.getId(), AppointmentStatus.BOOKED, dayStart, dayEnd);
        if (patientCount >= MAX_APPOINTMENTS_PER_DAY) {
            throw new BusinessException(
                    "Patient cannot book more than " + MAX_APPOINTMENTS_PER_DAY
                            + " appointments on the same day", HttpStatus.CONFLICT);
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setServiceType(request.getServiceType());
        appointment.setStartTime(start);
        appointment.setEndTime(end);
        appointment.setStatus(AppointmentStatus.BOOKED);

        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved, "Appointment booked successfully");
    }

    @Transactional
    public AppointmentResponse cancel(Long appointmentId) {

        Appointment appointment = findAppointment(appointmentId);

        // Only a booked appointment can move to CANCELED
        if (!appointment.getStatus().canTransitionTo(AppointmentStatus.CANCELED)) {
            throw new BusinessException(
                    "Only a booked appointment can be canceled", HttpStatus.CONFLICT);
        }

        // Rule 5: cancellation is allowed only before 2 hours of the start time
        LocalDateTime lastCancelTime = appointment.getStartTime().minusHours(CANCELLATION_HOURS);
        if (LocalDateTime.now().isAfter(lastCancelTime)) {
            throw new BusinessException(
                    "Cancellation is allowed only before " + CANCELLATION_HOURS
                            + " hours of the appointment start time", HttpStatus.CONFLICT);
        }

        appointment.setStatus(AppointmentStatus.CANCELED);
        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved, "Appointment canceled successfully");
    }

    // Rule 6: a canceled appointment cannot be booked again.
    // This endpoint always rejects, it only proves that the rule works.
    @Transactional(readOnly = true)
    public AppointmentResponse rebook(Long appointmentId) {

        Appointment appointment = findAppointment(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.CANCELED) {
            throw new BusinessException(
                    "A canceled appointment cannot be booked again", HttpStatus.CONFLICT);
        }
        throw new BusinessException("Appointment is already booked", HttpStatus.CONFLICT);
    }

    private Appointment findAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }

    private AppointmentResponse toResponse(Appointment appointment, String message) {
        AppointmentResponse response = new AppointmentResponse();
        response.setAppointmentId(appointment.getId());
        response.setDoctorId(appointment.getDoctor().getId());
        response.setDoctorName(appointment.getDoctor().getName());
        response.setPatientId(appointment.getPatient().getId());
        response.setPatientName(appointment.getPatient().getName());
        response.setServiceType(appointment.getServiceType());
        response.setStartTime(appointment.getStartTime());
        response.setEndTime(appointment.getEndTime());
        response.setStatus(appointment.getStatus());
        response.setMessage(message);
        return response;
    }
}
