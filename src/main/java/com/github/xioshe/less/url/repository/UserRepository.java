package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.repository.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<UserMapper, User> {
    public Optional<User> findByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).oneOpt();
    }

    public boolean existsByEmail(String email) {
        return lambdaQuery().eq(User::getEmail, email).eq(User::getStatus, 1).exists();
    }
}
