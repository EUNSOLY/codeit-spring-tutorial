package com.demo.demo;

public class User {

    private final String email;
    private final String name;
    private final boolean isDeleted;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.isDeleted = false;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }
}