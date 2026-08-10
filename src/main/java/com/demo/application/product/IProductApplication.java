package com.demo.application.product;

import com.demo.controller.internal.dto.ProductResponseDto;

import java.util.List;

public interface IProductApplication {
    List<ProductResponseDto> retrieve();

    ProductResponseDto retrieve(Integer id);
}
