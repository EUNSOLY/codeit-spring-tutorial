package com.demo.demo.order.exception;

import com.demo.demo.order.dto.OrderStatus;

import java.util.List;
import java.util.Map;

public class OrderStatusTransition {
    private static final Map<OrderStatus, List<OrderStatus>> TRANSITIONS = Map.of(
            OrderStatus.PENDING, List.of(OrderStatus.PAID, OrderStatus.CANCELLED),
            OrderStatus.PAID, List.of(OrderStatus.PREPARING, OrderStatus.CANCELLED),
            OrderStatus.PREPARING, List.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED),
            OrderStatus.SHIPPED, List.of(OrderStatus.DELIVERED),
            OrderStatus.DELIVERED, List.of(OrderStatus.REFUNDED),
            OrderStatus.CANCELLED, List.of(),
            OrderStatus.REFUNDED, List.of()
    );

    public static List<OrderStatus> getValidTransitions(OrderStatus currentStatus) {
        return TRANSITIONS.getOrDefault(currentStatus, List.of());
    }

    public static boolean canTransition(OrderStatus currentStatus, OrderStatus targetStatus) {
        return getValidTransitions(currentStatus).contains(targetStatus);
    }
}
