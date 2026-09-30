package org.example.managementsystem.repository;

import org.example.managementsystem.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByTitleIgnoreCase(String title);
}