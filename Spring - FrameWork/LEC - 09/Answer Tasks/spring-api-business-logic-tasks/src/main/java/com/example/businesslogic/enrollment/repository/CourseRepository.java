package com.example.businesslogic.enrollment.repository;

import com.example.businesslogic.enrollment.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}