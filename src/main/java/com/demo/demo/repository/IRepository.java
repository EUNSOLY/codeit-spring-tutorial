package com.demo.demo.repository;

import java.util.List;
import java.util.Optional;

import com.demo.demo.repository.entity.User;

public interface IRepository<T, ID> {
    Optional<T> findById(ID id);

    Optional<User> findByUsername(String username);

    List<T> findAll();

    T save(T entity);

    void delete(ID id);
}