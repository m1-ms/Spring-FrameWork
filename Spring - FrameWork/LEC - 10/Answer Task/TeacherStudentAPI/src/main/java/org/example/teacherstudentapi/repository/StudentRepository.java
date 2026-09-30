package org.example.teacherstudentapi.repository;

import org.example.teacherstudentapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}