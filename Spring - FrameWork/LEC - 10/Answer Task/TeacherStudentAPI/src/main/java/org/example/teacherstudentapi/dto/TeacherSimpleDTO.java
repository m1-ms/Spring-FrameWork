package org.example.teacherstudentapi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSimpleDTO {
    private Long id;
    private String name;
}