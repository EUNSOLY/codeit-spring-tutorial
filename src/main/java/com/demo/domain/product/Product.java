package com.demo.domain.product;

import lombok.Getter;

@Getter
public class Product {
    private static int PRODUCT_CURRENT_ID = 0;

    private static int idGenerate() {
        return ++PRODUCT_CURRENT_ID;
    }

    private Integer id;
    private String name; // 상품명
    private int price; // 가격
    private int stock; // 재고
    private boolean deleted = false; // 삭제여부

    private Product(Integer id, String name, int price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public static Product create(String name, int price, int stock) {
        int generatedId = idGenerate();
        return new Product(generatedId, name, price, stock);
    }
}
