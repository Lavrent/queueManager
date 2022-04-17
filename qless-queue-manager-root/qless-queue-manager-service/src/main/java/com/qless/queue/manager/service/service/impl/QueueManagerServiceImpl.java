package com.qless.queue.manager.service.service.impl;

import com.qless.queue.manager.service.enums.ServiceType;
import com.qless.queue.manager.service.enums.ServicerStatus;
import com.qless.queue.manager.service.model.ServiceQueue;
import com.qless.queue.manager.service.model.ServiceTypeQueueReport;
import com.qless.queue.manager.service.model.Customer;
import com.qless.queue.manager.service.model.Servicer;
import com.qless.queue.manager.service.service.QueueManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
public class QueueManagerServiceImpl implements QueueManagerService {
    private static final Map<Integer, ServiceQueue> queueMap = Map.of(1,
            ServiceQueue.builder()
                    .customers(new LinkedList<>())
                    .serviceTypes(List.of(ServiceType.CELL_PHONE_MONTHLY_PAYMENT,
                            ServiceType.INTERNET_MONTHLY_PAYMENT))
                    .servicers(List.of(Servicer.builder()
                            .id(1)
                            .name("Servicer1")
                            .build(), Servicer.builder()
                            .id(2)
                            .name("Servicer2")
                            .build()))
                    .build(), 2, ServiceQueue.builder()
                    .customers(new LinkedList<>())
                    .serviceTypes(List.of(ServiceType.CELL_PHONE_REPAIRS))
                    .servicers(List.of(Servicer.builder()
                                    .id(3)
                                    .name("Servicer3")
                                    .build(),
            Servicer.builder()
                    .id(4)
                    .name("Servicer4")
                    .build()))
                    .build());

    @Override
    public void addCustomerToQueue(Integer queueId, Customer customer) {
        if (!queueMap.containsKey(queueId)) {
            throw new IllegalArgumentException("Queue with such id does not exist");
        }

        ServiceQueue serviceQueue = queueMap.get(queueId);
        serviceQueue.validateServiceType(customer.getServiceTypeId());
        serviceQueue.addCustomer(customer);
    }

    @Override
    public ServiceQueue getQueue(Integer queueId) {
        if (!queueMap.containsKey(queueId)) {
            throw new IllegalArgumentException("Queue with such id does not exist");
        }

        return queueMap.get(queueId);
    }

    @Override
    public List<Customer> getQueueByServiceTypeId(Integer queueId, Integer serviceTypeId) {
        if (!queueMap.containsKey(queueId)) {
            throw new IllegalArgumentException("Queue with such id does not exist");
        }

        ServiceQueue serviceQueue = queueMap.get(queueId);
        serviceQueue.validateServiceType(serviceTypeId);

        return serviceQueue.getCustomers().stream().filter(customer -> customer.getServiceTypeId().equals(serviceTypeId)).collect(Collectors.toList());
    }

    @Override
    public List<ServiceTypeQueueReport> getServiceTypeQueueReport(Integer queueId) {
        if (!queueMap.containsKey(queueId)) {
            throw new IllegalArgumentException("Queue with such id does not exist");
        }

        ServiceQueue serviceQueue = queueMap.get(queueId);

        return serviceQueue.getCustomers().stream().collect(groupingBy(Customer::getServiceTypeId)).entrySet().stream().map(entry -> ServiceTypeQueueReport.builder().serviceType(ServiceType.getById(entry.getKey())).customerIds(entry.getValue().stream().map(Customer::getId).collect(Collectors.toList())).build()).collect(Collectors.toList());

    }

    @Override
    public void summonCustomer(Integer queueId) {
        if (!queueMap.containsKey(queueId)) {
            throw new IllegalArgumentException("Queue with such id does not exist");
        }

        ServiceQueue serviceQueue = queueMap.get(queueId);
        if (serviceQueue.getCustomers().isEmpty()) {
            throw new RuntimeException("There is no customer to serve currently");
        }

        Servicer freeServicer = serviceQueue.getServicers()
                .stream()
                .filter(servicer -> servicer.getStatus().equals(ServicerStatus.FREE))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("All servicers are currently busy"));

        freeServicer.changeStatus(ServicerStatus.BUSY);
        Customer customer = serviceQueue.getCustomers().poll();
        log.info("Servicer with id: " + freeServicer.getId() + " started serving customer with id: " + customer.getId());

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("Servicer with id: " + freeServicer.getId() + " finished serving customer with id: " + customer.getId());
        freeServicer.changeStatus(ServicerStatus.FREE);
    }
}