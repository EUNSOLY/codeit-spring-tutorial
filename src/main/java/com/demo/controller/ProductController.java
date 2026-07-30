package com.demo.controller;

import com.demo.dto.ProductCreateRequestDto;
import com.demo.dto.ProductResponseDto;
import com.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping(value = "")
    public ProductResponseDto create(
            @RequestBody ProductCreateRequestDto requestDto
    ) {
        try {
            return productService.create(requestDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/all")
    public List<ProductResponseDto> create(
            @RequestBody List<ProductCreateRequestDto> requestDtos
    ) {
        try {
            return productService.createAll(requestDtos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping(value = "")
    public List<ProductResponseDto> readAll(
            @RequestParam(required = false) String category
    ) {
        try {
            if (Objects.nonNull(category)) {
                return productService.findByCategory(category);

            }
            return productService.readAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/{id}")
    public ProductResponseDto findById(
            @PathVariable Integer id
    ) {
        try {
            return productService.findById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @DeleteMapping(value = "/{id}")
    public void deleteProduct(
            @PathVariable Integer id
    ) {
        try {
            productService.delete(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
