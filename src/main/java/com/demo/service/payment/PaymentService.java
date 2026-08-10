package com.demo.service.payment;

import com.demo.controller.internal.dto.PaymentCreateRequestDto;
import com.demo.controller.internal.dto.PaymentResponseDto;
import com.demo.controller.internal.dto.RequestingUserDto;
import com.demo.domain.payment.Payment;
import com.demo.domain.payment.PaymentStatus;
import com.demo.domain.product.Product;
import com.demo.repository.payment.PaymentRepository;
import com.demo.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;

    public PaymentResponseDto payment(
            @RequestBody PaymentCreateRequestDto request
    ) {

        List<Integer> productIds = request.getProductIds();
        Integer createdUserId = request.getRequestUserId();
        PaymentResponseDto.PaymentResponseDtoBuilder responseBuilder = PaymentResponseDto.builder();

        List<Product> products = new ArrayList<>();
        // 1. 구매하려는 상품이 존재하는지 + 상품의 재고가 충분한지 검증
        productIds.forEach(productId -> {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("찾으시는 상품이 존재하지 않습니다 - id : " + productId));
            if (product.getStock() < 1) {
                throw new RuntimeException("구매하시려는 상품의 재고가 존재하지 않습니다 - product: " + product);
            }
            products.add(product);
        });

        // 2. 실제 구매 완료
        Payment creating = Payment.create(products, createdUserId);
        creating.setStatus(PaymentStatus.PAYMENT_COMPLETE);
        creating.setPurchasedAt(LocalDateTime.now());
        creating.updated(createdUserId);
        Optional<Payment> wrappedCreated = paymentRepository.create(creating);
        Payment created = wrappedCreated
                .orElseThrow(() -> new RuntimeException("결제가 정상적으로 생성되지 않습니다"));

        responseBuilder.payment(created);


        // 3. 구매가 완료된 상품들에 대해서 재고 1개씩 차감
        for (Product product : products) {
            product.setStock(product.getStock() - 1);
            productRepository.update(product);
        }
        responseBuilder.products(products);
        return responseBuilder.build();
    }

    public PaymentResponseDto cancel(
            Integer id,
            RequestingUserDto requestingUser
    ) {
        Integer requestedUserId = requestingUser.getRequestUserId();
        PaymentResponseDto.PaymentResponseDtoBuilder responseBuilder = PaymentResponseDto.builder();

        // 1. 취소하려는 결제건이 존재하는지 확인
        Optional<Payment> wrappedPayment = paymentRepository.findById(id);
        Payment payment = wrappedPayment.orElseThrow(() -> new RuntimeException("취소하려는 결제가 존재하지 않습니다. - id : " + id));

        // 2. 취소 완료
        PaymentStatus currentStatus = payment.getStatus();
        if (!currentStatus.isCancellable()) {
            throw new RuntimeException("취소하시려는 결제는 취소할 수 없는 상태입니다 - id : " + payment.getId() + ", status : " + currentStatus);
        }
        payment.setStatus(PaymentStatus.CANCEL_COMPLETE);
        payment.setCancelledAt(LocalDateTime.now());
        payment.updated(requestedUserId);
        responseBuilder.payment(payment);

        // 3. 취소한 결제건에 들어있던 모든 상품들의 재고를 1 증가시키며 롤백
        List<Product> products = new ArrayList<>();
        List<Integer> productIds = payment.getProductIds();
        productIds.forEach(productId -> {
            Optional<Product> wrappedProduct = productRepository.findById(productId);
            Product product = wrappedProduct.orElseThrow(() -> new RuntimeException("결제한 상품이 존재하지 않습니다 - id : " + productId));
            product.setStock(product.getStock() + 1);
            productRepository.update(product);
            products.add(product);
        });
        responseBuilder.products(products);
        return responseBuilder.build();
    }
}
