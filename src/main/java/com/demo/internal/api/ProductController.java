package com.demo.internal.api;


import com.demo.domain.product.Product;
import com.demo.internal.dto.ProductResponseDto;
import com.demo.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping(value = "/internal/api/products")
    public List<ProductResponseDto> retrieve() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponseDto::from)
                .toList();
    }

    
    @GetMapping(value = "/internal/api/products/{id}")
    public ProductResponseDto retrieve(
            @PathVariable Integer id
    ) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("찾으시는 유저가 존재하지 않습니다"));

        return ProductResponseDto.from(product);
    }
}
