package org.example.teacherstudentapi.service;

import org.example.teacherstudentapi.dto.StudentSimpleDTO;
import org.example.teacherstudentapi.dto.TeacherDTO;
import org.example.teacherstudentapi.model.Student;
import org.example.teacherstudentapi.model.Teacher;
import org.example.teacherstudentapi.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TeacherDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        return convertToDTO(teacher);
    }

    private TeacherDTO convertToDTO(Teacher teacher) {
        List<StudentSimpleDTO> studentDTOs = teacher.getStudents()
                .stream()
                .map(s -> new StudentSimpleDTO(s.getId(), s.getName()))
                .collect(Collectors.toList());

        return new TeacherDTO(teacher.getId(), teacher.getName(), studentDTOs);
    }
}