package com.qless.queue.manager.service.enums;

public enum ServiceType {
    CELL_PHONE_MONTHLY_PAYMENT(1),
    INTERNET_MONTHLY_PAYMENT(2),
    CELL_PHONE_REPAIRS(3);

    private final int id;

    ServiceType(int id) {
        this.id = id;
    }

    public static ServiceType getById(Integer id) {
        for (ServiceType value : ServiceType.values()) {
            if (value.getId() == id) {
                return value;
            }
        }

        throw new IllegalArgumentException("Invalid service type id: " + id);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}