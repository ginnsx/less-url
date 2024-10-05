package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void update(User user) {
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(null);
        }
        if (!userRepository.updateById(user)) {
            throw new IllegalArgumentException("更新用户信息失败");
        }
    }
}
