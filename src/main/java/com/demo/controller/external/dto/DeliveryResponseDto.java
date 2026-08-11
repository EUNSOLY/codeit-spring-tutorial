package com.demo.controller.external.dto;

import com.demo.domain.payment.Payment;
import com.demo.domain.payment.PaymentStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryResponseDto {
    private final Integer id;
    private final PaymentStatus status;
    private final LocalDateTime deliveredAt;

    public static DeliveryResponseDto from(Payment entity) {
        return new DeliveryResponseDto(
                entity.getId(),
                entity.getStatus(),
                entity.getDeliveredAt()
        );
    }
}
