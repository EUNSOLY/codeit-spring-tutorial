package com.demo.internal.api;


import com.demo.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductController
 * - 리액트와 같은 CSR 즉 실제 고객의 앱/웹 브라우저로부터 실제 고객의 구매, 취소 기능 / 버튼에 대한 API 제공
 * 1. 고객이 코팡에 어떤 물건들이 있지? 하고 확인할 수 있는 전체 상품 조회
 * 2. 고객이 특정 상품에 대한 상세 정보를 볼 수 있게 할 단일 상품 조회
 * - API이기 때문에 @Controller + @ResponseBody을 같이 쓴 것과 같은 @RestController 사용
 */
@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
}
