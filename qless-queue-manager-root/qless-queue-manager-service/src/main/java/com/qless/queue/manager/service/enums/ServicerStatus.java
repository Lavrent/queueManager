package com.qless.queue.manager.service.enums;

public enum ServicerStatus {
    FREE, BUSY;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}