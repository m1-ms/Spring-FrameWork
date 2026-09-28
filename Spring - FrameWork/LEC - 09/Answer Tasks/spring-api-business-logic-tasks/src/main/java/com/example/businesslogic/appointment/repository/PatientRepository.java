package com.example.businesslogic.appointment.repository;

import com.example.businesslogic.appointment.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
