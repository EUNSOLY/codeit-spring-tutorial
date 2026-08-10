package com.demo.service.product;

import com.demo.controller.internal.dto.ProductResponseDto;
import com.demo.domain.product.Product;
import com.demo.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponseDto> retrieve() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponseDto::from)
                .toList();
    }


    public ProductResponseDto retrieve(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("찾으시는 유저가 존재하지 않습니다"));

        return ProductResponseDto.from(product);
    }
}
