package com.qless.queue.manager.service.service.impl;

import com.qless.queue.manager.service.configuration.QueueManagerConfiguration;
import com.qless.queue.manager.service.enums.ServiceType;
import com.qless.queue.manager.service.enums.ServicerStatus;
import com.qless.queue.manager.service.model.Customer;
import com.qless.queue.manager.service.model.ServiceQueue;
import com.qless.queue.manager.service.model.ServiceTypeQueueReport;
import com.qless.queue.manager.service.model.Servicer;
import com.qless.queue.manager.service.service.QueueManagerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@AllArgsConstructor
@Service
public class QueueManagerServiceImpl implements QueueManagerService {
    private QueueManagerConfiguration queueManagerConfiguration;

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

        Servicer freeServicer = serviceQueue.findFreeServicer();

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
        Map<Integer, ServiceQueue> queueMap = queueManagerConfiguration.queueMap();
        if (!queueMap.containsKey(queueId)) {
            throw new IllegalArgumentException("Queue with such id does not exist");
        }

        return queueMap.get(queueId);
    }
}