package com.github.xioshe.less.url.repository.auth;

import com.github.xioshe.less.url.entity.auth.User;
import com.github.xioshe.less.url.mapper.UserMapper;
import com.github.xioshe.less.url.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<UserMapper, User> {
    public Optional<User> findByEmail(String email) {
        return lambdaQuery().eq(User::getEmail, email).oneOpt();
    }

    public boolean existsByEmail(String email) {
        return lambdaQuery().eq(User::getEmail, email).eq(User::getStatus, 1).exists();
    }
}
