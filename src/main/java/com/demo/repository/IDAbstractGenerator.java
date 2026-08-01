package com.demo.repository;


public abstract class IDAbstractGenerator implements IDGenerator {
    private Integer id = 0;

    protected void changerId(Integer num) {
        id = num;
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
