package com.qless.queue.manager.web.controller;

import com.qless.queue.manager.service.enums.ServiceType;
import com.qless.queue.manager.service.model.Customer;
import com.qless.queue.manager.service.model.ServiceQueue;
import com.qless.queue.manager.service.model.ServiceTypeQueueReport;
import com.qless.queue.manager.service.model.Servicer;
import com.qless.queue.manager.service.service.QueueManagerService;
import com.qless.queue.manager.web.dto.CustomerDto;
import com.qless.queue.manager.web.dto.ServiceQueueDto;
import com.qless.queue.manager.web.dto.ServiceTypeQueueReportDto;
import com.qless.queue.manager.web.dto.ServicerDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

@AllArgsConstructor
@Component
@RestController
@RequestMapping("queues/{queueId}")
public class QueueManagerController {
    private final QueueManagerService queueManagerService;

    @PostMapping("customers")
    public void addCustomerToQueue(@PathVariable Integer queueId, @RequestBody CustomerDto customerDto) {
        Assert.notNull(queueId, "queueId cannot be null");
        Assert.notNull(customerDto, "customerDto cannot be null");
        Customer customer = mapToCustomer(customerDto);

        queueManagerService.addCustomerToQueue(queueId, customer);
    }

    private Customer mapToCustomer(CustomerDto customerDto) {
        return Customer.builder()
                .id(customerDto.getId())
                .name(customerDto.getName())
                .serviceTypeId(customerDto.getServiceTypeId())
                .build();
    }

    @GetMapping
    public ServiceQueueDto getQueue(@PathVariable Integer queueId) {
        ServiceQueue queue = queueManagerService.getQueue(queueId);

        return ServiceQueueDto.builder()
                .customers(queue.getCustomers()
                        .stream()
                        .map(this::mapToCustomerDto)
                        .collect(toCollection(LinkedList::new)))
                .servicers(queue.getServicers()
                        .stream()
                        .map(this::mapToServicerDto)
                        .toList())
                .serviceTypes(queue.getServiceTypes()
                        .stream()
                        .map(ServiceType::toString)
                        .toList())
                .build();
    }

    private ServicerDto mapToServicerDto(Servicer servicer) {
        return ServicerDto.builder()
                .id(servicer.getId())
                .name(servicer.getName())
                .status(servicer.getStatus().toString())
                .build();
    }

    @GetMapping("serviceTypes/{serviceTypeId}")
    public List<CustomerDto> getQueueByServiceType(@PathVariable Integer queueId, @PathVariable Integer serviceTypeId) {
        return queueManagerService.getQueueByServiceTypeId(queueId, serviceTypeId)
                .stream()
                .map(this::mapToCustomerDto)
                .toList();
    }

    private CustomerDto mapToCustomerDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .serviceTypeId(customer.getServiceTypeId())
                .build();
    }

    @GetMapping("report/serviceTypes")
    public List<ServiceTypeQueueReportDto> getServiceTypeQueueReport(@PathVariable Integer queueId) {
        List<ServiceTypeQueueReport> serviceTypeQueueReports = queueManagerService.getServiceTypeQueueReport(queueId);
        return serviceTypeQueueReports.stream()
                .map(this::mapToServiceTypeQueueReportDto)
                .toList();
    }

    private ServiceTypeQueueReportDto mapToServiceTypeQueueReportDto(ServiceTypeQueueReport report) {
        return ServiceTypeQueueReportDto.builder()
                .serviceType(report.getServiceType().toString())
                .customerIds(report.getCustomerIds())
                .build();
    }

    @PostMapping("customers/summon")
    public void summonCustomer(@PathVariable Integer queueId) {
        queueManagerService.summonCustomer(queueId);
    }
}