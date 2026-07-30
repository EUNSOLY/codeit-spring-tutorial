package com.demo.dto;

import com.demo.entity.Cart;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CartResponseDto {
    Integer id;
    Integer userId;
    List<CartResponseProductDto> products;

    public static CartResponseDto from(Cart cart, List<CartResponseProductDto> productDto) {
        return new CartResponseDto(cart.getId(), cart.getUserId(), productDto);
    }
}
