package org.example.teacherstudentapi.service;

import org.example.teacherstudentapi.dto.StudentDTO;
import org.example.teacherstudentapi.dto.TeacherSimpleDTO;
import org.example.teacherstudentapi.model.Student;
import org.example.teacherstudentapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return convertToDTO(student);
    }

    private StudentDTO convertToDTO(Student student) {
        List<TeacherSimpleDTO> teacherDTOs = student.getTeachers()
                .stream()
                .map(t -> new TeacherSimpleDTO(t.getId(), t.getName()))
                .collect(Collectors.toList());

        return new StudentDTO(student.getId(), student.getName(), teacherDTOs);
    }
}