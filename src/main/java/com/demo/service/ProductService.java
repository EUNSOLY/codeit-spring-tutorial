package com.demo.service;

import com.demo.dto.ProductCreateRequestDto;
import com.demo.dto.ProductResponseDto;
import com.demo.entity.Product;
import com.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponseDto findById(Integer id) {
        Product product = productRepository.read(id);
        if (Objects.isNull(product)) {
            throw new RuntimeException("찾는 상품이 없습니다.");
        }

        return ProductResponseDto.from(product);
    }

    public List<ProductResponseDto> findByCategory(String category) {
        List<Product> product = productRepository.findByCategory(category);
        return product.stream().map(ProductResponseDto::from).toList();
    }


    public List<ProductResponseDto> readAll() {
        List<Product> products = productRepository.readAll();
        return products.stream().map(ProductResponseDto::from).toList();
    }


    public ProductResponseDto create(ProductCreateRequestDto requestDto) {
        Product convetProduct = requestDto.toEntity();
        Product product = productRepository.create(convetProduct);
        return ProductResponseDto.from(product);
    }

    public List<ProductResponseDto> createAll(List<ProductCreateRequestDto> requestDtos) {
        List<ProductResponseDto> newProducts = new ArrayList<>();
        for (ProductCreateRequestDto requestDto : requestDtos) {
            Product convetProduct = requestDto.toEntity();
            Product product = productRepository.create(convetProduct);
            newProducts.add(ProductResponseDto.from(product));
        }

        return newProducts;
    }


    public void update(ProductCreateRequestDto requestDto) {

    }

    public void delete(Integer id) {
        ProductResponseDto productDto = this.findById(id);
        productRepository.delete(id);
    }
}
