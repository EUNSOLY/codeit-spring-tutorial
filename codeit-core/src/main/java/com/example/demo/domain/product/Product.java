package com.example.demo.domain.product;

import com.example.demo.domain.common.BaseEntity;
import lombok.Getter;
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

    // 재고 검증
    public void buyable() {
        if (this.stock < 1) {
            throw new RuntimeException("구매하시려는 상품의 재고가 존재하지 않습니다 - product: " + this.toString());
        }
    }

    // 재고 감소
    public void decrease() {
        this.stock -= 1;
    }

    // 재고 증가
    public void increase() {
        this.stock += 1;
    }

    public void update(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        super.updated();
    }
}
