package org.example.managementsystem.controller;

import org.example.managementsystem.dto.CourseWithStudentsDTO;
import org.example.managementsystem.dto.InstructorDetailsDTO;
import org.example.managementsystem.dto.StudentDTO;
import org.example.managementsystem.model.Course;
import org.example.managementsystem.model.Instructor;
import org.example.managementsystem.model.Student;
import org.example.managementsystem.repository.CourseRepository;
import org.example.managementsystem.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping
    public ResponseEntity<?> createInstructor(@RequestBody Instructor instructor) {
        if (instructorRepository.findByEmailIgnoreCase(instructor.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("An instructor with this email already exists.");
        }
        Instructor saved = instructorRepository.save(instructor);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<Course>> getCoursesByInstructor(@PathVariable Long id) {
        Optional<Instructor> instructorOpt = instructorRepository.findById(id);

        if (instructorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(instructorOpt.get().getCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstructorDetailsDTO> getInstructorDetails(@PathVariable Long id) {
        Optional<Instructor> instructorOpt = instructorRepository.findById(id);

        if (instructorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Instructor instructor = instructorOpt.get();

        List<CourseWithStudentsDTO> courseDTOs = new ArrayList<>();

        for (Course course : instructor.getCourses()) {
            List<StudentDTO> studentDTOs = new ArrayList<>();
            for (Student student : course.getStudents()) {
                studentDTOs.add(new StudentDTO(student.getId(), student.getName(), student.getEmail()));
            }

            courseDTOs.add(new CourseWithStudentsDTO(
                    course.getId(),
                    course.getTitle(),
                    course.getDescription(),
                    studentDTOs
            ));
        }

        InstructorDetailsDTO dto = new InstructorDetailsDTO(
                instructor.getId(),
                instructor.getName(),
                instructor.getEmail(),
                instructor.getPhoneNumber(),
                instructor.getAge(),
                courseDTOs
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInstructor(@PathVariable Long id, @RequestBody Instructor updatedInstructor) {
        Optional<Instructor> instructorOpt = instructorRepository.findById(id);

        if (instructorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Instructor> existingWithEmail = instructorRepository.findByEmailIgnoreCase(updatedInstructor.getEmail());
        if (existingWithEmail.isPresent() && !existingWithEmail.get().getId().equals(id)) {
            return ResponseEntity.badRequest().body("An instructor with this email already exists.");
        }

        Instructor instructor = instructorOpt.get();
        instructor.setName(updatedInstructor.getName());
        instructor.setEmail(updatedInstructor.getEmail());
        instructorRepository.save(instructor);

        return ResponseEntity.ok(instructor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        Optional<Instructor> instructorOpt = instructorRepository.findById(id);

        if (instructorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Instructor instructor = instructorOpt.get();

        for (Course course : new ArrayList<>(instructor.getCourses())) {
            course.setInstructor(null);
            courseRepository.save(course);
        }

        instructorRepository.delete(instructor);
        return ResponseEntity.noContent().build();
    }
}