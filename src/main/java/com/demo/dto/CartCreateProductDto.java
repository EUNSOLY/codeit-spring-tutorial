package com.demo.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CartCreateProductDto {
    Integer productId;
    Integer quantity; // 갯수

    public static CartCreateProductDto from(CartCreateProductDto cartProductDto) {
        return new CartCreateProductDto(cartProductDto.getProductId(), cartProductDto.getQuantity());
    }
}
