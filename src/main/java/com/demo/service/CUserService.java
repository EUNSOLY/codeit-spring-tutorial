package com.demo.service;

import com.demo.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CUserService implements UserServiceInterface {
    private static final List<User> USERS = new ArrayList<>() {
        {
            add(new User(1, "Aaron", 15, "Developer", "Backend"));
            add(new User(1, "Baron", 20, "Developer", "Frontend"));
            add(new User(1, "Caron", 30, "Developer", "Infra"));
            add(new User(1, "Daron", 25, "Developer", "Designer"));
            add(new User(1, "Earon", 21, "Developer", "Backend"));
        }
    };


    @Override
    public User findById(Integer id) {
        return USERS.getFirst();

//        return USERS.stream()
//                .filter(user -> user.getId() == id)
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("찾으시는 유저가 없습니다."));
    }

    public List<User> findAll() {
        return USERS;
    }
}
