package com.demo.demo.repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.demo.demo.repository.entity.User;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository implements IRepository<User, Integer> {
    private final PasswordEncoder passwordEncoder;
    private static final Map<Integer, User> users = new HashMap<>();

    @PostConstruct
    public void init() {
        users.put(1,
            new User(1, "aaron", passwordEncoder.encode("123"), "Aaron", 10, "DEVELOPER", "Backend", User.ADMIN_ROLES,
                LocalDateTime.now().plusMinutes(10))
        );
        users.put(2,
            new User(2, "baron", passwordEncoder.encode("123"), "Baron", 20, "DEVELOPER", "Frontend", User.SIMPLE_ROLES,
                LocalDateTime.now().plusMinutes(20))
        );
        users.put(3, new User(3, "caron", passwordEncoder.encode("123"), "Caron", 30, "ENGINEER", "DevOps/SRE",
            User.SIMPLE_ROLES, LocalDateTime.now().plusMinutes(30))
        );
    }

    private int idGenerate() {
        return Collections.max(users.keySet()) + 1;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
            .filter(item -> item.getName().equalsIgnoreCase(username))
            .findAny();
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
