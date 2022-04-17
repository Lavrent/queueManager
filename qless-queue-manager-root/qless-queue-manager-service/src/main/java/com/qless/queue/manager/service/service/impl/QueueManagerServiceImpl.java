package com.qless.queue.manager.service.service.impl;

import com.qless.queue.manager.service.enums.ServiceType;
import com.qless.queue.manager.service.enums.ServicerStatus;
import com.qless.queue.manager.service.model.Customer;
import com.qless.queue.manager.service.model.ServiceQueue;
import com.qless.queue.manager.service.model.ServiceTypeQueueReport;
import com.qless.queue.manager.service.model.Servicer;
import com.qless.queue.manager.service.service.QueueManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
public class QueueManagerServiceImpl implements QueueManagerService {
    private static final Map<Integer, ServiceQueue> queueMap = Map.of(1, ServiceQueue.builder()
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

    @Override
    public void addCustomerToQueue(Integer queueId, Customer customer) {
        ServiceQueue serviceQueue = findQueueById(queueId);
        serviceQueue.validateServiceType(customer.getServiceTypeId());
        serviceQueue.addCustomer(customer);
    }

    @Override
    public ServiceQueue getQueue(Integer queueId) {
        return findQueueById(queueId);
    }

    @Override
    public List<Customer> getQueueByServiceTypeId(Integer queueId, Integer serviceTypeId) {
        ServiceQueue serviceQueue = findQueueById(queueId);
        serviceQueue.validateServiceType(serviceTypeId);

        return serviceQueue.getCustomers()
                .stream()
                .filter(customer -> customer.getServiceTypeId().equals(serviceTypeId))
                .toList();
    }

    @Override
    public List<ServiceTypeQueueReport> getServiceTypeQueueReport(Integer queueId) {
        ServiceQueue serviceQueue = findQueueById(queueId);

        return serviceQueue.getCustomers()
                .stream()
                .collect(groupingBy(Customer::getServiceTypeId))
                .entrySet()
                .stream()
                .map(this::createReport)
                .toList();

    }

    private ServiceTypeQueueReport createReport(Entry<Integer, List<Customer>> entry) {
        return ServiceTypeQueueReport.builder()
                .serviceType(ServiceType.getById(entry.getKey()))
                .customerIds(entry.getValue()
                        .stream()
                        .map(Customer::getId)
                        .toList())
                .build();
    }

    @Override
    public void summonCustomer(Integer queueId) {
        ServiceQueue serviceQueue = findQueueById(queueId);
        serviceQueue.validateCustomers();

        Servicer freeServicer = serviceQueue.findFirstCustomer();

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

    private ServiceQueue findQueueById(Integer queueId) {
        if (!queueMap.containsKey(queueId)) {
            throw new IllegalArgumentException("Queue with such id does not exist");
        }

        return queueMap.get(queueId);
    }
}