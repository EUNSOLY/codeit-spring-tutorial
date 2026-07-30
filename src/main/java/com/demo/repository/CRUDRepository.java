package com.demo.repository;

import java.util.List;

public interface CRUDRepository<T, K> {
    T read(K k);

    List<T> readAll();

    T create(T t);

    void update(T t);

    void delete(K k);
}
