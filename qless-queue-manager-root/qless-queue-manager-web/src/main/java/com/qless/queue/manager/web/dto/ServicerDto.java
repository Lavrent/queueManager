package com.qless.queue.manager.web.dto;

import com.qless.queue.manager.service.enums.ServicerStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Servicer {
    private Integer id;
    private String name;
    @Builder.Default
    private ServicerStatus status = ServicerStatus.FREE;

    public void changeStatus(ServicerStatus status) {
        this.status = status;
    }
}