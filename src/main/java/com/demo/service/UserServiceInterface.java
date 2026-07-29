package com.demo.service;


import com.demo.entity.User;

import java.util.List;

public interface UserServiceInterface {
    User findById(Integer id);

    List<User> findAll();
}