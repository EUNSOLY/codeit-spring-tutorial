package com.demo.application.payment;

import com.demo.controller.internal.dto.PaymentResponseDto;

import java.util.List;

public interface IPaymentApplication {
    PaymentResponseDto payment(List<Integer> productIds, Integer requestedUserId);

    PaymentResponseDto cancel(Integer id, Integer requestedUserId);
}
