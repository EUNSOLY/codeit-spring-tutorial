package com.demo.internal.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PaymentCreateRequestDto extends RequestingUserDto {
    private final List<Integer> productIds;

    public PaymentCreateRequestDto(Integer requestUserId, List<Integer> productIds) {
        super(requestUserId);
        this.productIds = productIds;
    }
}
