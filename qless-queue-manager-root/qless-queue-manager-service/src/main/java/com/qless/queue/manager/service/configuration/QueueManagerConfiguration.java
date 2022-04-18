package com.qless.queue.manager.service.configuration;

import com.qless.queue.manager.service.enums.ServiceType;
import com.qless.queue.manager.service.model.ServiceQueue;
import com.qless.queue.manager.service.model.Servicer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Configuration
public class QueueManagerConfiguration {

    @Bean
    public Map<Integer, ServiceQueue> queueMap() {
        return Map.of(1, ServiceQueue.builder()
                        .customers(new LinkedList<>())
                        .serviceTypes(List.of(
                                ServiceType.CELL_PHONE_MONTHLY_PAYMENT,
                                ServiceType.INTERNET_MONTHLY_PAYMENT))
                        .servicers(List.of(
                                Servicer.builder().id(1).name("Servicer1").build(),
                                Servicer.builder().id(2).name("Servicer2").build()))
                        .build(),
                2, ServiceQueue.builder()
                        .customers(new LinkedList<>())
                        .serviceTypes(List.of(ServiceType.CELL_PHONE_REPAIRS))
                        .servicers(List.of(
                                Servicer.builder().id(3).name("Servicer3").build(),
                                Servicer.builder().id(4).name("Servicer4").build()))
                        .build());
    }
}