package com.example.businesslogic.enrollment.entity;

import com.example.businesslogic.enrollment.enums.StudentLevel;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ENR_COURSES")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "MAX_STUDENTS", nullable = false)
    private int maxStudents;

    // Null means the course is open for every level
    @Enumerated(EnumType.STRING)
    @Column(name = "MINIMUM_LEVEL")
    private StudentLevel minimumLevel;

    @Column(name = "DROP_DEADLINE", nullable = false)
    private LocalDate dropDeadline;

    @ManyToMany
    @JoinTable(name = "ENR_COURSE_PREREQUISITES",
            joinColumns = @JoinColumn(name = "COURSE_ID"),
            inverseJoinColumns = @JoinColumn(name = "PREREQUISITE_ID"))
    private Set<Course> prerequisites = new HashSet<>();

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public StudentLevel getMinimumLevel() {
        return minimumLevel;
    }

    public void setMinimumLevel(StudentLevel minimumLevel) {
        this.minimumLevel = minimumLevel;
    }

    public LocalDate getDropDeadline() {
        return dropDeadline;
    }

    public void setDropDeadline(LocalDate dropDeadline) {
        this.dropDeadline = dropDeadline;
    }

    public Set<Course> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(Set<Course> prerequisites) {
        this.prerequisites = prerequisites;
    }
}
