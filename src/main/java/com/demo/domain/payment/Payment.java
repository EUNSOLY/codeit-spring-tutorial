package com.demo.domain.payment;

import com.demo.domain.common.BaseEntity;
import com.demo.domain.product.Product;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Payment extends BaseEntity {
    private static int PAYMENT_CURRENT_ID = 0;

    private static int idGenerate() {
        return ++PAYMENT_CURRENT_ID;
    }

    private List<Integer> productIds; // 구매할 상품의 고유값들
    private PaymentStatus status = PaymentStatus.IN_PAYMENT; // 결제 상태
    private int paidPrice; // 결제 가격
    private LocalDateTime purchasedAt; // 결제 완료 시점
    private LocalDateTime deliveredAt; // 배송 완료 시점
    private LocalDateTime cancelledAt; // 취소 완료 시점


    private Payment(Integer id, Integer createdByUserId, List<Integer> productIds, int paidPrice) {
        super(id, createdByUserId);
        this.productIds = productIds;
        this.paidPrice = paidPrice;
    }


    public static Payment create(List<Product> products, Integer createdByUserId) {
        int generatedId = idGenerate();

        List<Integer> productIds = products.stream().map(Product::getId).toList();
        int paidPrice = products.stream()
                .map(Product::getPrice)
                .reduce(0, Integer::sum);
        return new Payment(generatedId, createdByUserId, productIds, paidPrice);
    }
}
