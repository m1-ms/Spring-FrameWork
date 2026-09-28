package com.example.businesslogic.appointment.enums;

public enum ServiceType {

    CONSULTATION(30),
    CHECKUP(45),
    FOLLOW_UP(15);

    private final int durationMinutes;

    ServiceType(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
