package com.demo.repository;

import java.util.List;

public interface IRepository<T, K> {
    T create(T entity);

    List<T> readAll();

    T read(K id);

    T update(T entity);

    T delete(K id);
}
