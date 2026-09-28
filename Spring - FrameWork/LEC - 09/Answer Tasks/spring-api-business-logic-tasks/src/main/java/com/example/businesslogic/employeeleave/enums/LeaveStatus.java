package com.example.businesslogic.employeeleave.enums;

public enum LeaveStatus {

    PENDING,
    APPROVED,
    REJECTED,
    CANCELED;

    // Active requests are the ones that hold days from the employee balance
    public boolean isActive() {
        return this == PENDING || this == APPROVED;
    }

    // The valid status flow
    public boolean canTransitionTo(LeaveStatus next) {
        return switch (this) {
            case PENDING -> next == APPROVED || next == REJECTED || next == CANCELED;
            case APPROVED -> next == CANCELED;
            case REJECTED, CANCELED -> false;
        };
    }
}
