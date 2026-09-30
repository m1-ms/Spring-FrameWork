package org.example.managementsystem.controller;

import org.example.managementsystem.dto.CourseWithInstructorDTO;
import org.example.managementsystem.dto.InstructorDTO;
import org.example.managementsystem.dto.StudentDetailsDTO;
import org.example.managementsystem.model.Course;
import org.example.managementsystem.model.Instructor;
import org.example.managementsystem.model.Student;
import org.example.managementsystem.repository.CourseRepository;
import org.example.managementsystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        if (studentRepository.findByEmailIgnoreCase(student.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("A student with this email already exists.");
        }
        Student saved = studentRepository.save(student);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @PostMapping("/{studentId}/register/{courseId}")
    public ResponseEntity<?> registerStudentToCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {

        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (studentOpt.isEmpty() || courseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentOpt.get();
        Course course = courseOpt.get();

        if (student.getCourses().contains(course)) {
            return ResponseEntity.badRequest().body("This student is already registered in this course.");
        }

        student.getCourses().add(course);
        studentRepository.save(student);

        return ResponseEntity.ok(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailsDTO> getStudentDetails(@PathVariable Long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);

        if (studentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentOpt.get();

        List<CourseWithInstructorDTO> courseDTOs = new ArrayList<>();

        for (Course course : student.getCourses()) {
            InstructorDTO instructorDTO = null;
            if (course.getInstructor() != null) {
                Instructor instructor = course.getInstructor();
                instructorDTO = new InstructorDTO(instructor.getId(), instructor.getName(), instructor.getEmail());
            }

            courseDTOs.add(new CourseWithInstructorDTO(
                    course.getId(),
                    course.getTitle(),
                    course.getDescription(),
                    instructorDTO
            ));
        }

        StudentDetailsDTO dto = new StudentDetailsDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getAge(),
                student.getActive(),
                courseDTOs
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        Optional<Student> studentOpt = studentRepository.findById(id);

        if (studentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Student> existingWithEmail = studentRepository.findByEmailIgnoreCase(updatedStudent.getEmail());
        if (existingWithEmail.isPresent() && !existingWithEmail.get().getId().equals(id)) {
            return ResponseEntity.badRequest().body("A student with this email already exists.");
        }

        Student student = studentOpt.get();
        student.setName(updatedStudent.getName());
        student.setEmail(updatedStudent.getEmail());
        studentRepository.save(student);

        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}/toggle-active")
    public ResponseEntity<Student> toggleActive(@PathVariable Long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);

        if (studentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentOpt.get();
        boolean currentlyActive = Boolean.TRUE.equals(student.getActive());
        student.setActive(!currentlyActive);
        studentRepository.save(student);

        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);

        if (studentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        studentRepository.delete(studentOpt.get());
        return ResponseEntity.noContent().build();
    }
}