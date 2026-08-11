package com.example.demo.controller;

import com.example.demo.application.payment.DeliveryApplication;
import com.example.demo.controller.dto.DeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    private final DeliveryApplication deliveryApplication;

    @RequestMapping(method = RequestMethod.PATCH, value = "/external/api/payments/{id}/in-delivery")
    public DeliveryResponseDto delivery(@PathVariable Integer id) {
        return deliveryApplication.delivery(id);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/external/api/payments/{id}/delivery-complete")
    public DeliveryResponseDto delivered(@PathVariable Integer id) {
        return deliveryApplication.delivered(id);
    }
}
