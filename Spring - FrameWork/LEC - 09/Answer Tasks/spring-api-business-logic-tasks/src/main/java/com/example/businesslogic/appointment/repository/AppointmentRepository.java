package com.example.businesslogic.appointment.repository;

import com.example.businesslogic.appointment.entity.Appointment;
import com.example.businesslogic.appointment.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT COUNT(a) FROM Appointment a " +
            "WHERE a.doctor.id = :doctorId AND a.status = :status " +
            "AND a.startTime < :newEnd AND a.endTime > :newStart")
    long countOverlapping(@Param("doctorId") Long doctorId,
                          @Param("status") AppointmentStatus status,
                          @Param("newStart") LocalDateTime newStart,
                          @Param("newEnd") LocalDateTime newEnd);

    @Query("SELECT COUNT(a) FROM Appointment a " +
            "WHERE a.patient.id = :patientId AND a.status = :status " +
            "AND a.startTime >= :dayStart AND a.startTime < :dayEnd")
    long countPatientAppointmentsBetween(@Param("patientId") Long patientId,
                                         @Param("status") AppointmentStatus status,
                                         @Param("dayStart") LocalDateTime dayStart,
                                         @Param("dayEnd") LocalDateTime dayEnd);
}