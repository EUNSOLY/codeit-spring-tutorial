package com.demo.demo.security;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 가장 이상적인건 사실
 * - UserDetails implements 해서 User 라는 Entity Class 를 정의하여 우리가 사용하려는 Class 와 Security 목적을 동시에 이룬것처럼
 * - UserDetailsService implements 해서 UserService 라는 @Service Class 를 정의하여 우리가 사용하려는 Serive 와 Security 목적을 동시에 이루는것
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        log.info("CustomUserDetailService 진입");
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다 - username : " + username));
    }
}
