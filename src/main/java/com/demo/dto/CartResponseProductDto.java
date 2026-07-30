package com.demo.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CartResponseProductDto {
    Integer id;
    String productName;
    Integer quantity;

    public static CartResponseProductDto from(ProductResponseDto product, Integer quantity) {
        return new CartResponseProductDto(
                product.getId(),
                product.getTitle(),
                quantity
        );
    }
}
