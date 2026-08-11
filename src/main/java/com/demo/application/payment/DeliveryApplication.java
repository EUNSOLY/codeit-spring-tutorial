package com.demo.application.payment;


import com.demo.controller.external.dto.DeliveryResponseDto;
import com.demo.domain.payment.Payment;
import com.demo.domain.user.User;
import com.demo.service.payment.PaymentService;
import com.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryApplication {
    private final PaymentService paymentService;
    private final UserService userService;

    public DeliveryResponseDto delivery(Integer paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        payment.delivering();
        return DeliveryResponseDto.from(payment);
    }

    public DeliveryResponseDto delivered(Integer paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        payment.delivered();
        Integer paidUserId = payment.getCreatedBy();
        User user = userService.getUser(paidUserId);
        user.earn(payment.getPaidPrice());
        return DeliveryResponseDto.from(payment);
    }
}
