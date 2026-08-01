package com.demo.repository;

import java.util.List;

public interface IRepository<K, T> {
    T create(T entity);

    List<T> readAll();

    T read(K id);

    T update(T entity);

    void delete(K id);
}