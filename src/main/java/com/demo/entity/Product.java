package com.demo.entity;

import com.demo.enums.ProductCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Product {
    Integer id;
    String title;
    double price;
    String description;
    String image;
    ProductCategory category;
}
