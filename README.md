# Queue Manager Application

## Description
Queue Manager is a multi module application, which designed to manage 2 queues each having 2 servicers.

## Project Structure
Queue Manager Application is a multi module RESTful application. It is designed in terms of layered architecture concept.

## Endpoints
* `addCustomerToQueue` - Adds customer to queue if queue id and customer are valid
* `getQueue` - Returns queue info if given queue id is valid
* `getQueueByServiceType`- Returns queue info filtered by service type if queue id and service type id are valid
* `getServiceTypeQueueReport`-Returns how many customer ids are in queue for each service type
* `summonCustomer` - Serves first customer in queue and removes customer after service.

## Functionalities
* Validations - Several validations have been done to be sure that application will behave as needed in different cases
* Exception handling - `QueueManagerExceptionHandler` is a centralized exception handler which handles different type of exceptions(both custom and unexpected) and returns error response dto with corresponding message

## What could be done
* The storage system could be a database instead of inmemory. In that case we would have additional repository layer with all the database related logic
* Security could be implemented for some endpoints (for example `getQueue` endpoint could be accessible only for that queue servicer