package com.demo.entity;

import com.demo.dto.CartCreateProductDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Cart {
    Integer id;
    Integer userId;
    LocalDate date;
    List<CartCreateProductDto> productDtos;
}
