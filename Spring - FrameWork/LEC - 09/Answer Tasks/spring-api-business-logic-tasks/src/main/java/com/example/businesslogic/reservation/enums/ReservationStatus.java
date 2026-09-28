package com.example.businesslogic.reservation.enums;

public enum ReservationStatus {

    PENDING,
    CONFIRMED,
    CANCELED,
    EXPIRED;

    // Only pending and confirmed reservations are still active
    public boolean isActive() {
        return this == PENDING || this == CONFIRMED;
    }
}
