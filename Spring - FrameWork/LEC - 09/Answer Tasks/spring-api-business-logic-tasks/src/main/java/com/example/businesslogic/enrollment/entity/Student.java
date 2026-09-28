package com.example.businesslogic.enrollment.entity;

import com.example.businesslogic.enrollment.enums.StudentLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "ENR_STUDENTS")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "STUDENT_NAME", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "STUDENT_LEVEL", nullable = false)
    private StudentLevel level;

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StudentLevel getLevel() {
        return level;
    }

    public void setLevel(StudentLevel level) {
        this.level = level;
    }
}
