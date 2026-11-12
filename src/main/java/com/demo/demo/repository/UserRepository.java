package com.demo.demo.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.demo.demo.repository.entity.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository implements IRepository<User, Integer> {
    private static final Map<Integer, User> users = new HashMap<>();

    private int idGenerate() {
        return Collections.max(users.keySet()) + 1;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public User save(User entity) {
        int generatedId = idGenerate();
        entity.setId(generatedId);
        users.put(generatedId, entity);
        return users.get(generatedId);
    }

    @Override
    public void delete(Integer id) {
        users.remove(id);
    }
}
