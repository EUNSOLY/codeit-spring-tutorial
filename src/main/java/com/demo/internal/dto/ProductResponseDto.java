package com.demo.internal.dto;

import com.demo.domain.product.Product;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductResponseDto {
    private final Integer id;
    private final String name;
    private final int price;
    private final int stock;


    public static ProductResponseDto from(Product entity) {
        return new ProductResponseDto(entity.getId(), entity.getName(), entity.getPrice(), entity.getStock());
    }
}
