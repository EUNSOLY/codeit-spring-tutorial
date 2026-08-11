package com.demo.controller.admin.api;

import com.demo.controller.internal.dto.RequestingUserDto;
import com.demo.domain.product.Product;
import lombok.Getter;

@Getter
public class ProductAdminUpsertRequestDto extends RequestingUserDto {
    private final String name;
    private final int price;
    private final int stock;

    public ProductAdminUpsertRequestDto(
            String name,
            int price,
            int stock,
            Integer requestUserId
    ) {
        super(requestUserId);
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Product toEntity() {
        return Product.create(super.requestUserId, this.name, this.price, this.stock);
    }
}
