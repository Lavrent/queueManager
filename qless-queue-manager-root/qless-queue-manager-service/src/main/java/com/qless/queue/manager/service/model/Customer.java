package com.qless.queue.manager.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class Customer {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private Integer serviceTypeId;
}