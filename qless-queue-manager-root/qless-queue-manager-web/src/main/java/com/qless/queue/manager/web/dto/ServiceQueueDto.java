package com.qless.queue.manager.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Queue;

@Getter
@Builder
public class ServiceQueueDto {
    private List<ServicerDto> servicers;
    private Queue<CustomerDto> customers;
    private List<String> serviceTypes;
}