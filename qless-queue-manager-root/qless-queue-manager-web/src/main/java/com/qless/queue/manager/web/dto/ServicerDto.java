package com.qless.queue.manager.web.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServicerDto {
    private Integer id;
    private String name;
    private String status;
}