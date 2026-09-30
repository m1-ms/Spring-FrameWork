package org.example.managementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDetailsDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Integer age;
    private List<CourseWithStudentsDTO> courses;
}