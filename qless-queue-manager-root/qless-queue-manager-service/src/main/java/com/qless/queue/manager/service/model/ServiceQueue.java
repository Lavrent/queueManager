package com.qless.queue.manager.service.model;

import com.qless.queue.manager.service.enums.ServiceType;
import com.qless.queue.manager.service.enums.ServicerStatus;
import com.qless.queue.manager.service.exception.CustomerAlreadyExistsException;
import com.qless.queue.manager.service.exception.NotFoundException;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Queue;

@Getter
@Builder
public class ServiceQueue {
    private List<Servicer> servicers;
    private Queue<Customer> customers;
    private List<ServiceType> serviceTypes;

    public void addCustomer(Customer customer) {
        if (customers.stream()
                .map(Customer::getId)
                .anyMatch(id -> id.equals(customer.getId()))) {
            throw new CustomerAlreadyExistsException("Customer with this id already exists in queue");
        }

        customers.add(customer);
    }

    public void validateServiceType(Integer serviceTypeId) {
        ServiceType serviceType = ServiceType.getById(serviceTypeId);
        if (!serviceTypes.contains(serviceType)) {
            throw new IllegalArgumentException("Current queue does not support service type: " + serviceType
                    + ".\nSupported service types are " + serviceTypes);
        }
    }

    public void validateCustomers(){
        if (customers.isEmpty()) {
            throw new NotFoundException("There is no customer to serve currently");
        }
    }

    public Servicer findFreeServicer() {
        return servicers
                .stream()
                .filter(servicer -> ServicerStatus.FREE.equals(servicer.getStatus()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("All servicers are currently busy"));
    }
}