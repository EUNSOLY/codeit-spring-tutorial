package com.demo.domain.payment;

import com.demo.domain.common.BaseEntity;
import com.demo.domain.product.Product;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString(callSuper = true)
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


    // 결제 진행
    public void complete(Integer requestedUserId) {
        if (!requestedUserId.equals(super.createdBy)) {
            throw new IllegalArgumentException("주문한 유져와 결제를 수행한 유저가 다릅니다 - requestedUserId : " + requestedUserId + " != paymentUserId: " + super.createdBy);
        }
        if (this.status.compareTo(PaymentStatus.PAYMENT_COMPLETE) > 0) {
            throw new RuntimeException("결제 완료로 상태를 바꿀 수 없는 결제건입니다 - payment : " + this.toString());
        }

        this.status = PaymentStatus.PAYMENT_COMPLETE;
        this.purchasedAt = LocalDateTime.now();
        super.updated(requestedUserId);
    }

    public void cancel(Integer requestedUserId) {
        if (!requestedUserId.equals(super.createdBy)) {
            throw new RuntimeException("취소하려는 유져와 취소하려는 결제를 수행한 유저가 다릅니다 - requestedUserId : " + requestedUserId + " != paymentUserId: " + super.createdBy);
        }
        if (!this.status.isCancellable()) {
            throw new RuntimeException("취소하시려는 결제는 취소할 수 없는 상태입니다 - id : " + this.id + ", status : " + this.status);
        }
        
        this.status = PaymentStatus.CANCEL_COMPLETE;
        this.cancelledAt = LocalDateTime.now();
        super.updated(requestedUserId);
    }
}
