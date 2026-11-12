package com.demo.demo.repository.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class User implements UserDetails {
    @Setter
    private Integer id;
    private String username;
    private String password;
    private String name;
    private Integer age;
    private String job;
    private String specialty;
    private List<SimpleGrantedAuthority> authorities;
    private LocalDateTime createdAt;

    public final static SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");
    public final static SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
    public final static List<SimpleGrantedAuthority> SIMPLE_ROLES = List.of(ROLE_USER);
    public final static List<SimpleGrantedAuthority> ADMIN_ROLES = List.of(ROLE_USER, ROLE_ADMIN);

    public static User create(String username, String password, String name, Integer age, String job,
        String specialty) {
        return new User(
            null,
            username,
            password,
            name,
            age,
            job,
            specialty,
            SIMPLE_ROLES,
            LocalDateTime.now()
        );
    }
}