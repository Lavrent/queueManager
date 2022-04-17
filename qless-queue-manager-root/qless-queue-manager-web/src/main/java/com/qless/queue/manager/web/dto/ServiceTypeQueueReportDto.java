package com.qless.queue.manager.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class ServiceTypeQueueReportDto {
    private String serviceType;
    private List<Integer> customerIds;
}