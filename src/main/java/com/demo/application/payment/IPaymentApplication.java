package com.demo.application.payment;

import com.demo.controller.internal.dto.PaymentResponseDto;

import java.util.List;

public interface IPaymentApplication {
    PaymentResponseDto payment(List<Integer> productIds);

    PaymentResponseDto cancel(Integer id);
}
