package com.demo.demo.order.exception;

import com.demo.demo.exception.ErrorCode;
import com.demo.demo.order.dto.OrderStatus;

import java.util.List;

public class InvalidOrderStatusTransitionException extends OrderException {

    public InvalidOrderStatusTransitionException(
            Long orderId,
            OrderStatus currentStatus,
            OrderStatus targetStatus
    ) {
        super(ErrorCode.INVALID_ORDER_STATUS, String.format("주문 상태를 %s에서 %s로 변경할 수 없습니다", currentStatus.name(), targetStatus.name()));

        super.addContext("orderId", orderId);
        super.addContext("currentStatus", currentStatus.name());
        super.addContext("targetStatus", targetStatus.name());
        super.addContext("validTransitions", getValidTransitions(currentStatus));
    }

    private List<OrderStatus> getValidTransitions(OrderStatus currentStatus) {
        // 현재 상태에서 가능한 전이 상태들을 반환// 비즈니스 규칙에 따라 구현
        return OrderStatusTransition.getValidTransitions(currentStatus);
    }
}
