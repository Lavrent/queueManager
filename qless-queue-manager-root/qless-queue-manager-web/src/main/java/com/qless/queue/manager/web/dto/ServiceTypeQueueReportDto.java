package com.qless.queue.manager.web.dto;

import com.qless.queue.manager.service.enums.ServiceType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ServiceTypeQueueReport {
    private ServiceType serviceType;
    private List<Integer> customerIds;
}