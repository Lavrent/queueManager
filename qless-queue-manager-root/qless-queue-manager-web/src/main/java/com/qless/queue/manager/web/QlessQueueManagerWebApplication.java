package com.qless.queue.manager.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.qless.queue.manager")
@SpringBootApplication
public class QlessQueueManagerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(QlessQueueManagerWebApplication.class, args);
    }

}