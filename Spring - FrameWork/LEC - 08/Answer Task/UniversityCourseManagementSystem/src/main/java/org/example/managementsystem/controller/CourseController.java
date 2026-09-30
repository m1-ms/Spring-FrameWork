package org.example.managementsystem.controller;

import org.example.managementsystem.dto.CourseDetailsDTO;
import org.example.managementsystem.dto.InstructorDTO;
import org.example.managementsystem.dto.StudentDTO;
import org.example.managementsystem.model.Course;
import org.example.managementsystem.model.Instructor;
import org.example.managementsystem.model.Student;
import org.example.managementsystem.repository.CourseRepository;
import org.example.managementsystem.repository.InstructorRepository;
import org.example.managementsystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        if (courseRepository.findByTitleIgnoreCase(course.getTitle()).isPresent()) {
            return ResponseEntity.badRequest().body("A course with this title already exists.");
        }
        Course saved = courseRepository.save(course);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @PutMapping("/{courseId}/assign-instructor/{instructorId}")
    public ResponseEntity<?> assignInstructor(
            @PathVariable Long courseId,
            @PathVariable Long instructorId) {

        Optional<Course> courseOpt = courseRepository.findById(courseId);
        Optional<Instructor> instructorOpt = instructorRepository.findById(instructorId);

        if (courseOpt.isEmpty() || instructorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Course course = courseOpt.get();
        course.setInstructor(instructorOpt.get());
        courseRepository.save(course);

        return ResponseEntity.ok(course);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailsDTO> getCourseDetails(@PathVariable Long id) {
        Optional<Course> courseOpt = courseRepository.findById(id);

        if (courseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Course course = courseOpt.get();

        InstructorDTO instructorDTO = null;
        if (course.getInstructor() != null) {
            Instructor instructor = course.getInstructor();
            instructorDTO = new InstructorDTO(instructor.getId(), instructor.getName(), instructor.getEmail());
        }

        List<StudentDTO> studentDTOs = new ArrayList<>();
        for (Student student : course.getStudents()) {
            studentDTOs.add(new StudentDTO(student.getId(), student.getName(), student.getEmail()));
        }

        CourseDetailsDTO dto = new CourseDetailsDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getActive(),
                instructorDTO,
                studentDTOs
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        Optional<Course> courseOpt = courseRepository.findById(id);

        if (courseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Course course = courseOpt.get();
        course.setTitle(updatedCourse.getTitle());
        course.setDescription(updatedCourse.getDescription());
        courseRepository.save(course);

        return ResponseEntity.ok(course);
    }

    @PutMapping("/{id}/toggle-active")
    public ResponseEntity<Course> toggleActive(@PathVariable Long id) {
        Optional<Course> courseOpt = courseRepository.findById(id);

        if (courseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Course course = courseOpt.get();
        boolean currentlyActive = Boolean.TRUE.equals(course.getActive());
        course.setActive(!currentlyActive);
        courseRepository.save(course);

        return ResponseEntity.ok(course);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        Optional<Course> courseOpt = courseRepository.findById(id);

        if (courseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Course course = courseOpt.get();

        for (Student student : new ArrayList<>(course.getStudents())) {
            student.getCourses().remove(course);
            studentRepository.save(student);
        }

        courseRepository.delete(course);
        return ResponseEntity.noContent().build();
    }
}