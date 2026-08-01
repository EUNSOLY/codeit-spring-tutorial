package com.demo.repository;

public abstract class IdGenerator {
    private Integer id = 1;

    protected Integer increase() {
        return id++;
    }

    protected void decrease() {
        id--;
    }
}
