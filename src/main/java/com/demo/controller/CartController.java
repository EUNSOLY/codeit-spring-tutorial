package com.demo.controller;

import com.demo.dto.CartCreateRequestDto;
import com.demo.dto.CartResponseDto;
import com.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping(value = "")
    public CartResponseDto create(
            @RequestBody CartCreateRequestDto requestDto
    ) {
        try {
            return cartService.create(requestDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "")
    public List<CartResponseDto> readAll() {
        try {
            return cartService.readAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/{id}")
    public CartResponseDto read(
            @PathVariable Integer id
    ) {
        try {
            return cartService.findById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Integer id
    ) {
        try {
            cartService.delete(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
