package com.demo.repository;

import com.demo.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepository implements CRUDRepository<Product, Integer> {
    private final Map<Integer, Product> DATABASE = new HashMap<>();
    private int sequence = 0; // 로직 AI 도움 (인메모리디비ID값 자동 생성)

    @Override
    public Product read(Integer id) {
        return this.DATABASE.get(id);
    }

    @Override
    public List<Product> readAll() {
        return this.DATABASE.values().stream().toList();
    }

    @Override
    public Product create(Product product) {
        int newId = ++sequence;
        Product savedProduct = new Product(newId, product.getTitle(), product.getPrice(), product.getDescription(), product.getImage(), product.getCategory());
        DATABASE.put(newId, savedProduct);
        return savedProduct;
    }

    @Override
    public void update(Product product) {
        this.DATABASE.replace(product.getId(), product);
    }

    @Override
    public void delete(Integer id) {
        this.DATABASE.remove(id);
    }

    public List<Product> findByCategory(String category) {
        return this.DATABASE.values().stream()
                .filter(product -> product.getCategory().getCategoryName().equals(category))
                .toList();
    }
}
