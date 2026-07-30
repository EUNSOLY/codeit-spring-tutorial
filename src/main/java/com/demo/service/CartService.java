package com.demo.service;

import com.demo.dto.*;
import com.demo.entity.Cart;
import com.demo.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartResponseDto findById(Integer id) {
        Cart cart = cartRepository.read(id);
        List<CartResponseProductDto> converterProducts = converterProducts(cart.getProductDtos());
        return CartResponseDto.from(cart, converterProducts);
    }


    private List<CartResponseProductDto> converterProducts(List<CartCreateProductDto> cartCreateProductDtos) {
        List<CartResponseProductDto> cartProductDtos = new ArrayList<>();
        cartCreateProductDtos.forEach(cartProductDto -> {
            ProductResponseDto product = productService.findById(cartProductDto.getProductId());
            CartResponseProductDto cartResProductDto = CartResponseProductDto.from(product, cartProductDto.getQuantity());
            cartProductDtos.add(cartResProductDto);
        });
        return cartProductDtos;
    }

    public List<CartResponseDto> readAll() {
        List<Cart> carts = cartRepository.readAll();
        return carts.stream().map(cart -> {
                    List<CartResponseProductDto> converterProducts = converterProducts(cart.getProductDtos());
                    return CartResponseDto.from(cart, converterProducts);
                }
        ).toList();
    }


    public CartResponseDto create(CartCreateRequestDto requestDto) {
        Cart convetCart = requestDto.toEntity();
        Cart cart = cartRepository.create(convetCart);
        List<CartResponseProductDto> converterProducts = converterProducts(cart.getProductDtos());
        return CartResponseDto.from(cart, converterProducts);
    }

    public void delete(Integer id) {
        this.findById(id);
        cartRepository.delete(id);
    }
}
