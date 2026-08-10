package com.demo.application.product;

import com.demo.controller.internal.dto.ProductResponseDto;
import com.demo.domain.product.Product;
import com.demo.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductApplication implements IProductApplication {
    private final ProductService productService;


    @Override
    public List<ProductResponseDto> retrieve() {
        List<Product> products = productService.getProducts();
        return products.stream()
                .map(ProductResponseDto::from)
                .toList();
    }

    @Override
    public ProductResponseDto retrieve(Integer id) {
        Product retrieved = productService.getProduct(id);
        return ProductResponseDto.from(retrieved);
    }

}
