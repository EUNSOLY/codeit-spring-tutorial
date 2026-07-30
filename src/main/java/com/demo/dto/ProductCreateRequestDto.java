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
public class ProductCreateRequestDto {
    String title;
    double price;
    String description;
    String image;
    String category;


    public Product toEntity() {
        return new Product(0, this.title, this.price, this.description, this.image, ProductCategory.toCategory(this.category));
    }
}
