package com.demo.controller.admin.dto;

import com.demo.domain.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductAdminResponseDto {
    private final Integer id;
    private final String name;
    private final int price;
    private final int stock;
    private final boolean deleted;

    public static ProductAdminResponseDto from(Product entity) {
        return new ProductAdminResponseDto(entity.getId(), entity.getName(), entity.getPrice(), entity.getStock(), entity.isDeleted());
    }
}
