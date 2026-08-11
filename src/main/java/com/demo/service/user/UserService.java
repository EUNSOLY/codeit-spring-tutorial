package com.demo.service.user;

import com.demo.domain.user.User;
import com.demo.repository.IRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IRepository<Integer, User> userRepository;

    public User getUser(Integer id) {
        Optional<User> wrappedUser = userRepository.findById(id);
        return wrappedUser
                .orElseThrow(() -> new RuntimeException("찾으시는 유저가 존재하지 않습니다"));
    }

    public Optional<User> findUser(Integer id) {
        return userRepository.findById(id);
    }

}
