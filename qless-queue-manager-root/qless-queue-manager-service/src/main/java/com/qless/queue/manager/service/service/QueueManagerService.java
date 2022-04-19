package com.qless.queue.manager.service.service;

import com.qless.queue.manager.service.model.Customer;
import com.qless.queue.manager.service.model.ServiceQueue;
import com.qless.queue.manager.service.model.ServiceTypeQueueReport;

import java.util.List;

public interface QueueManagerService {

    void addCustomerToQueue(Integer queueId, Customer customer);

    ServiceQueue getQueue(Integer queueId);

    List<Customer> getQueueByServiceTypeId(Integer queueId, Integer serviceTypeId);

    List<ServiceTypeQueueReport> getServiceTypeQueueReport(Integer queueId);

    void summonCustomer(Integer queueId);
}