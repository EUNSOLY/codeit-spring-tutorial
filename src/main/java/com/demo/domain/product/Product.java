package com.demo.domain.product;

import com.demo.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class Product extends BaseEntity {
    private static int PRODUCT_CURRENT_ID = 0;

    private static int idGenerate() {
        return ++PRODUCT_CURRENT_ID;
    }

    private String name; // 상품명
    private int price; // 가격
    @Setter
    private int stock; // 재고


    private Product(Integer id, Integer createdByUserId, String name, int price, int stock) {
        super(id, createdByUserId);
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public static Product create(Integer createdByUserId, String name, int price, int stock) {
        int generatedId = idGenerate();
        return new Product(generatedId, createdByUserId, name, price, stock);
    }
}
