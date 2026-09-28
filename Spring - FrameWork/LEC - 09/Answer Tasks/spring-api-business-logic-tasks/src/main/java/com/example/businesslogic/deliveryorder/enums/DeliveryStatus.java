package com.example.businesslogic.deliveryorder.enums;

public enum DeliveryStatus {

    CREATED,
    ASSIGNED,
    PICKED_UP,
    IN_TRANSIT,
    DELIVERED,
    CANCELED;

    // The valid status flow
    public boolean canTransitionTo(DeliveryStatus next) {
        return switch (this) {
            case CREATED -> next == ASSIGNED || next == CANCELED;
            case ASSIGNED -> next == PICKED_UP || next == CANCELED;
            case PICKED_UP -> next == IN_TRANSIT;
            case IN_TRANSIT -> next == DELIVERED;
            case DELIVERED, CANCELED -> false;
        };
    }
}
