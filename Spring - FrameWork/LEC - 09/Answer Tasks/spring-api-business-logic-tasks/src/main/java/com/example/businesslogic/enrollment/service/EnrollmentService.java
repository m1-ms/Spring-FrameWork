package com.example.businesslogic.enrollment.service;

import com.example.businesslogic.common.exception.BusinessException;
import com.example.businesslogic.common.exception.ResourceNotFoundException;
import com.example.businesslogic.enrollment.dto.EnrollRequest;
import com.example.businesslogic.enrollment.dto.EnrollmentResponse;
import com.example.businesslogic.enrollment.entity.Course;
import com.example.businesslogic.enrollment.entity.Enrollment;
import com.example.businesslogic.enrollment.entity.Student;
import com.example.businesslogic.enrollment.enums.EnrollmentStatus;
import com.example.businesslogic.enrollment.repository.CourseRepository;
import com.example.businesslogic.enrollment.repository.EnrollmentRepository;
import com.example.businesslogic.enrollment.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(StudentRepository studentRepository,
                             CourseRepository courseRepository,
                             EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public EnrollmentResponse enroll(EnrollRequest request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // Rule 1: a student cannot enroll in the same course twice
        boolean alreadyIn = enrollmentRepository.existsByStudentIdAndCourseIdAndStatusIn(
                student.getId(), course.getId(),
                List.of(EnrollmentStatus.ENROLLED, EnrollmentStatus.PASSED));
        if (alreadyIn) {
            throw new BusinessException(
                    "Student is already enrolled in (or has passed) this course", HttpStatus.CONFLICT);
        }

        // Rule 2: some courses are available only from a specific student level
        if (course.getMinimumLevel() != null
                && student.getLevel().getOrder() < course.getMinimumLevel().getOrder()) {
            throw new BusinessException("This course is not available for the student's level");
        }

        // Rule 3 and 4: every prerequisite must be passed
        for (Course prerequisite : course.getPrerequisites()) {
            boolean passed = enrollmentRepository.existsByStudentIdAndCourseIdAndStatus(
                    student.getId(), prerequisite.getId(), EnrollmentStatus.PASSED);
            if (passed) {
                continue;
            }
            boolean failed = enrollmentRepository.existsByStudentIdAndCourseIdAndStatus(
                    student.getId(), prerequisite.getId(), EnrollmentStatus.FAILED);
            if (failed) {
                throw new BusinessException(
                        "Student failed the required prerequisite: " + prerequisite.getTitle());
            }
            throw new BusinessException(
                    "Student has not completed the required prerequisite: " + prerequisite.getTitle());
        }

        // Rule 5: the course cannot exceed its maximum number of students
        long enrolledCount = enrollmentRepository
                .countByCourseIdAndStatus(course.getId(), EnrollmentStatus.ENROLLED);
        if (enrolledCount >= course.getMaxStudents()) {
            throw new BusinessException("Course is full", HttpStatus.CONFLICT);
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ENROLLED);
        enrollment.setEnrollmentDate(LocalDate.now());

        Enrollment saved = enrollmentRepository.save(enrollment);
        return toResponse(saved, "Enrolled successfully");
    }

    @Transactional
    public EnrollmentResponse drop(Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        if (enrollment.getStatus() != EnrollmentStatus.ENROLLED) {
            throw new BusinessException("Only an active enrollment can be dropped", HttpStatus.CONFLICT);
        }

        // Rule 6: dropping is allowed only before the deadline
        if (LocalDate.now().isAfter(enrollment.getCourse().getDropDeadline())) {
            throw new BusinessException("The drop deadline has passed", HttpStatus.CONFLICT);
        }

        // Rule 7: the seat becomes available again because the capacity is calculated from the number of ENROLLED students
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        Enrollment saved = enrollmentRepository.save(enrollment);
        return toResponse(saved, "Course dropped. The seat is available again");
    }

    private EnrollmentResponse toResponse(Enrollment enrollment, String message) {
        EnrollmentResponse response = new EnrollmentResponse();
        response.setEnrollmentId(enrollment.getId());
        response.setStudentId(enrollment.getStudent().getId());
        response.setCourseId(enrollment.getCourse().getId());
        response.setCourseTitle(enrollment.getCourse().getTitle());
        response.setStatus(enrollment.getStatus());
        response.setEnrollmentDate(enrollment.getEnrollmentDate());
        response.setMessage(message);
        return response;
    }
}
