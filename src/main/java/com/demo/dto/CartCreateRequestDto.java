package com.demo.dto;

import com.demo.entity.Cart;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CartCreateRequestDto {
    Integer userId;
    LocalDate date;
    List<CartCreateProductDto> products;

    public Cart toEntity() {
        return new Cart(0, this.userId, this.date, this.products);
    }
}
