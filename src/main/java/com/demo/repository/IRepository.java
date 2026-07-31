package com.demo.repository;

import com.demo.entity.Member;

import java.util.List;

public interface IRepository {
    Member create(Member entity);

    List<Member> readAll();

    Member read(Integer id);

    Member update(Member entity);

    void delete(Integer id);
}