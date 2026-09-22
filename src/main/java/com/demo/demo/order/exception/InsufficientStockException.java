package com.demo.demo.order.exception;

import com.demo.demo.exception.ErrorCode;

/**
 * 재고 부족으로 주문을 완료할 수 없을 때 발생하는 예외
 * 단순히 "재고 부족"이라는 메시지보다는 구체적인 정보를 제공한다
 */
public class InsufficientStockException extends OrderException {
    protected InsufficientStockException(Long productId, int requestedQuantity, int availableQuantity) {
        super(ErrorCode.INSUFFICIENT_STOCK, String.format("재고가 부족합니다. 요청: %d개, 재고: %d개",
                requestedQuantity, availableQuantity));
        super.addContext("productId", productId);
        super.addContext("requestedQuantity", requestedQuantity);
        super.addContext("availableQuantity", availableQuantity);
        super.addContext("shortfall", requestedQuantity - availableQuantity);
    }

}




