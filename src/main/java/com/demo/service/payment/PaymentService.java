package com.demo.service.payment;

import com.demo.domain.payment.Payment;
import com.demo.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;


/**
 * PaymentService
 * - Service 명칭 자체가 기본적으로 도메인 서비스를 의미 Domain Service
 * = Domain Service <- Domain Repository
 * - Payment 도메인(엔티티 객체)을 파라미터로 받거나 반환값으로 반환하는
 * = PaymentService 와 외부 Application 의 관계는
 * - 외부 Application 에게 Payment 반환해주거나
 * - 외부 Application 로부터 Payment 받아서 그것에 대한 처리를 해주거나
 * * 처리 : CRUD 에 국한된다 / 주의 ! 도메인 내부 상태를 바꾸는 메서드는 Application 에서 호출할것 !
 * - Domain Service 서비스의 강제사항은 단 하나의 Domain Repository 만 필드로 가져야한다는것
 */
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment getPayment(Integer id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾으시는 결제가 존재하지 않습니다"));
    }

    public Payment create(Payment entity) {
        Optional<Payment> wrappedCreated = paymentRepository.create(entity);
        return wrappedCreated
                .orElseThrow(() -> new IllegalArgumentException("결제가 정상적으로 생성되지 않습니다"));
    }

    public Payment update(Payment entity) {
        Optional<Payment> wrappedCreated = paymentRepository.update(entity);
        return wrappedCreated.orElseThrow(() -> new IllegalArgumentException("업데이트가 정상적으로 되지 않습니다"));
    }


}
