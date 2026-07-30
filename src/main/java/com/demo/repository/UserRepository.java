package com.demo.repository;

import com.demo.entity.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository implements CRUDRepository<User, Integer> {
    private final Map<Integer, User> DATABASE = new HashMap<>();
    private int sequence = 0; // 로직 AI 도움 (인메모리디비ID값 자동 생성)

    @Override
    public User read(Integer id) {
        return this.DATABASE.get(id);
    }

    @Override
    public List<User> readAll() {
        return this.DATABASE.values().stream().toList();
    }

    @Override
    public User create(User user) {
        int newId = ++sequence;
        User savedUser = new User(newId, user.getName(), user.getAge(), user.getJob(), user.getSpecialty());
        DATABASE.put(newId, savedUser);
        return savedUser;
    }

    @Override
    public void update(User user) {
        this.DATABASE.replace(user.getId(), user);
    }

    @Override
    public void delete(Integer id) {
        this.DATABASE.remove(id);
    }
}
