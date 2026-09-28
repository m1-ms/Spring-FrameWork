package com.example.businesslogic.appointment.repository;

import com.example.businesslogic.appointment.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
