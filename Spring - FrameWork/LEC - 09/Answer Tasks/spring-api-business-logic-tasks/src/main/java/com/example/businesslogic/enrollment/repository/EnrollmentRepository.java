package com.example.businesslogic.enrollment.repository;

import com.example.businesslogic.enrollment.entity.Enrollment;
import com.example.businesslogic.enrollment.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // How many students are currently enrolled in the course
    long countByCourseIdAndStatus(Long courseId, EnrollmentStatus status);

    // Does the student have an enrollment with this exact status in the course
    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, EnrollmentStatus status);

    // Does the student have an enrollment in the course with any of these statuses
    boolean existsByStudentIdAndCourseIdAndStatusIn(Long studentId, Long courseId, Collection<EnrollmentStatus> statuses);
}
