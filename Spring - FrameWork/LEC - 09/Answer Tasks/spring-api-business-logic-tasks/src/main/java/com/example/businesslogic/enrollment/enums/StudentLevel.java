package com.example.businesslogic.enrollment.enums;

public enum StudentLevel {

    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    LEVEL_4(4);

    private final int order;

    StudentLevel(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
