package com.demo.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, K> {
    T create(T entity);

    List<T> readAll();

    Optional<T> read(K id);

    Optional<T> update(T entity);

    Optional<T> delete(K id);
}
