package com.demo.external.api;

import com.demo.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * DeliveryController
 * - 로젠택배와 같은 배송대행업체에서 코팡에서 고객이 구매한 상품의 배송을 시작했는지 / 잘마쳤는지 여부를 쿠팡인 우리에게 알려주기 위해 호출하는 API
 * 1. 고객이 구매한 구매건의 상품에 대한 배송을 시작했을때
 * 2. 고객이 구매한 구매건의 상품에 대한 배송이 완료됐을때
 * - API이기 때문에 @Controller + @ResponseBody을 같이 쓴 것과 같은 @RestController 사용
 */

@RestController
@RequiredArgsConstructor
public class DeliveryController {
    private final PaymentRepository paymentRepository;
}
