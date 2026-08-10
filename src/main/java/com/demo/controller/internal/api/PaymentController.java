package com.demo.controller.internal.api;

import com.demo.application.payment.IPaymentApplication;
import com.demo.controller.internal.dto.PaymentCreateRequestDto;
import com.demo.controller.internal.dto.PaymentResponseDto;
import com.demo.controller.internal.dto.RequestingUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PaymentController
 * - 리액트와 같은 CSR 즉 실제 고객의 앱/웹 브라우저로부터 실제 고객의 구매, 취소 기능 / 버튼에 대한 API 제공
 * 1. 고객이 어떤 상품들을 구매할지 보내면 그 고객에게 해당 상품의 구매 정보를 생성
 * 2. 고객이 기존에 구매했던 구매건을 취소하는 경우 - Hard Delete 가 아닌 Soft Delete 상태변경으로
 * - API이기 때문에 @Controller + @ResponseBody을 같이 쓴 것과 같은 @RestController 사용
 */
@RestController
@RequiredArgsConstructor
public class PaymentController {
    /**
     * Hexagonal (Port and Adaptor) 아키텍쳐 도입 시
     * 동일 내용
     */
    private final IPaymentApplication paymentApplication;


    @PostMapping(value = "/internal/api/payments")
    public PaymentResponseDto payment(
            @RequestBody PaymentCreateRequestDto request
    ) {
        List<Integer> productIds = request.getProductIds();
        Integer requestedUserId = request.getRequestUserId();
        return paymentApplication.payment(productIds, requestedUserId);
    }


    @PatchMapping(value = "/internal/api/payments/{id}/cancel")
    public PaymentResponseDto cancel(
            @PathVariable Integer id,
            @RequestBody RequestingUserDto requestingUser
    ) {
        int requestUserId = requestingUser.getRequestUserId();
        return paymentApplication.cancel(id, requestUserId);
    }
}
