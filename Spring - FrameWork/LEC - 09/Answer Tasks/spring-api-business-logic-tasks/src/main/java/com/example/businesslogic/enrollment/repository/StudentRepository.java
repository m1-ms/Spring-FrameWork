package com.example.businesslogic.enrollment.repository;

import com.example.businesslogic.enrollment.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
