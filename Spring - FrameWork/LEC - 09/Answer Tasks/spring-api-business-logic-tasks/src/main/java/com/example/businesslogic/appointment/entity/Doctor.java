package com.example.businesslogic.appointment.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "APPT_DOCTORS")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DOCTOR_NAME", nullable = false)
    private String name;

    @Column(name = "IS_AVAILABLE", nullable = false)
    private boolean available;

    @Column(name = "WORK_START_HOUR", nullable = false)
    private int workStartHour;

    @Column(name = "WORK_END_HOUR", nullable = false)
    private int workEndHour;

    public Doctor() {
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getWorkStartHour() {
        return workStartHour;
    }

    public void setWorkStartHour(int workStartHour) {
        this.workStartHour = workStartHour;
    }

    public int getWorkEndHour() {
        return workEndHour;
    }

    public void setWorkEndHour(int workEndHour) {
        this.workEndHour = workEndHour;
    }
}
