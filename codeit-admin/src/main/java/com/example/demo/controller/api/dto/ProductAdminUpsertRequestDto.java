package com.example.demo.controller.api.dto;

import com.example.demo.domain.product.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProductAdminUpsertRequestDto extends RequestingUserDto {
    //@NotNull          // null 허용X
    //@NotEmpty         // null 허용X  + "" 빈String 허용X
    @NotBlank           // null 허용ㅌ + "" 빈String 허용X + " " 공백 허용X
    private final String name;
    @Min(10000)
    private final int price;
    @Max(100)
    private final int stock;

    public ProductAdminUpsertRequestDto(String name, int price, int stock, Integer requestUserId) {
        super(requestUserId);
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Product toEntity() {
        return Product.create(super.requestUserId, this.name, this.price, this.stock);
    }
}
