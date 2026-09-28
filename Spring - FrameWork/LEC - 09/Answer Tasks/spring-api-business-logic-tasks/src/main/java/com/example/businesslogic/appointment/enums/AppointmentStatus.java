package com.example.businesslogic.appointment.enums;

public enum AppointmentStatus {

    BOOKED,
    CANCELED;

    public boolean canTransitionTo(AppointmentStatus next) {
        return this == BOOKED && next == CANCELED;
    }
}