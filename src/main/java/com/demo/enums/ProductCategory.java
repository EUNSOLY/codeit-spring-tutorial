package com.demo.enums;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ProductCategory {
    FOOD("식품"),
    FASHION("패션"),
    ELECTRONICS("전자제품"),
    HOME("생활용품"),
    SPORTS("스포츠/레저");

    String categoryName;

    public static ProductCategory toCategory(String categoryName) {
        for (ProductCategory productCategory : ProductCategory.values()) {
            if (productCategory.getCategoryName().equals(categoryName)) {
                return productCategory;
            }
        }
        throw new RuntimeException("일치하는 카테고리가 존재하지않습니다.");
    }

}
