package com.demo.dto;

import com.demo.entity.Product;
import com.demo.enums.ProductCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductResponseDto {
    Integer id;
    String title;
    double price;
    String description;
    String image;
    ProductCategory category;

    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                product.getImage(),
                product.getCategory()
        );
    }
}
