package com.demo.repository;

import com.demo.entity.Member;

import java.util.List;

public class AbstractRepository implements IRepository, IDGenerator {
    protected static Integer id = 0;


    @Override
    public Member create(Member entity) {
        return null;
    }

    @Override
    public List<Member> readAll() {
        return List.of();
    }

    @Override
    public Member read(Integer id) {
        return null;
    }

    @Override
    public Member update(Member entity) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Integer nextId() {
        return id++;
    }

    @Override
    public Integer previousId() {
        return id--;
    }
}
