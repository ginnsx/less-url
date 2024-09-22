package com.github.xioshe.less.url.service;


import com.github.xioshe.less.url.api.dto.LoginCommand;
import com.github.xioshe.less.url.api.dto.SignupCommand;
import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.repository.mapper.UserMapper;
import com.github.xioshe.less.url.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtTokenService tokenService;

    public User signup(SignupCommand command) {
        User user = command.asUser(passwordEncoder);
        Long id = userMapper.insertSelective(user);
        return userMapper.selectByPrimaryKey(id);
    }

    public User authenticate(LoginCommand command) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.getUsername(),
                        command.getPassword()
                )
        );

        User user = userMapper.findByUsername(command.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

    public void logout(String token) {
        tokenService.blacklistToken(token);
    }
}
