package com.demo.repository;

import com.demo.entity.Cart;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CartRepository implements CRUDRepository<Cart, Integer> {
    private final Map<Integer, Cart> DATABASE = new HashMap<>();
    private int sequence = 0; // 로직 AI 도움 (인메모리디비ID값 자동 생성)

    @Override
    public Cart read(Integer id) {
        return this.DATABASE.get(id);
    }

    @Override
    public List<Cart> readAll() {
        return this.DATABASE.values().stream().toList();
    }

    @Override
    public Cart create(Cart cart) {
        int newId = ++sequence;
        Cart savedCart = new Cart(newId, cart.getUserId(), cart.getDate(), cart.getProductDtos());
        DATABASE.put(newId, savedCart);
        return savedCart;
    }

    @Override
    public void update(Cart cart) {
        this.DATABASE.replace(cart.getId(), cart);
    }

    @Override
    public void delete(Integer id) {
        this.DATABASE.remove(id);
    }
}
