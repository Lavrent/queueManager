package com.qless.queue.manager.web.controller;

import com.qless.queue.manager.web.dto.Customer;
import com.qless.queue.manager.web.dto.ServiceQueue;
import com.qless.queue.manager.web.dto.ServiceTypeQueueReport;
import com.qless.queue.manager.service.service.QueueManagerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Component
@RestController()
public class QueueManagerController {
    private final QueueManagerService queueManagerService;


    @PostMapping("queues/{queueId}/customers")
    public void addCustomerToQueue(@PathVariable Integer queueId, @RequestBody Customer customer) {
        queueManagerService.addCustomerToQueue(queueId, customer);
    }

    @GetMapping("queues/{queueId}")
    public ServiceQueue getQueue(@PathVariable Integer queueId) {
        return queueManagerService.getQueue(queueId);
    }

    @GetMapping("queues/{queueId}/serviceTypes/{serviceTypeId}")
    public List<Customer> getQueueByServiceType(@PathVariable Integer queueId, @PathVariable Integer serviceTypeId) {
        return queueManagerService.getQueueByServiceTypeId(queueId, serviceTypeId);
    }

    @GetMapping("queues/{queueId}/serviceTypes/report")
    public List<ServiceTypeQueueReport> getServiceTypeQueueReport(@PathVariable Integer queueId) {
        return queueManagerService.getServiceTypeQueueReport(queueId);
    }

    @PostMapping("queues/{queueId}/customers/summon")
    public void summonCustomer(@PathVariable Integer queueId) {
        queueManagerService.summonCustomer(queueId);
    }
}