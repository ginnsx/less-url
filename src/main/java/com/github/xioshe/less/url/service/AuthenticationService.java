package com.github.xioshe.less.url.service;


import com.github.xioshe.less.url.api.dto.AuthCommand;
import com.github.xioshe.less.url.api.dto.AuthResponse;
import com.github.xioshe.less.url.api.dto.SignupCommand;
import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.repository.mapper.UserMapper;
import com.github.xioshe.less.url.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtTokenService tokenService;
    private final UserDetailsService userDetailsService;

    public User signup(SignupCommand command) {
        User user = command.asUser(passwordEncoder);
        Long id = userMapper.insertSelective(user);
        return userMapper.selectByPrimaryKey(id);
    }

    public AuthResponse login(AuthCommand command) {
        UserDetails user = authenticate(command.getUsername(), command.getPassword());
        return generateAuth(user);
    }

    public UserDetails authenticate(String username, String password) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        return (UserDetails) authenticate.getPrincipal();
    }

    public AuthResponse generateAuth(UserDetails securityUser) {
        String accessToken = tokenService.generateAccessToken(securityUser);
        String refreshToken = tokenService.generateRefreshToken(securityUser);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .expiresIn(tokenService.getAccessTokenExpiration())
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        String username = tokenService.extractUsername(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(username);
        boolean isValid = tokenService.isRefreshTokenValid(refreshToken, user);
        if (!isValid) {
            throw new BadCredentialsException("refresh token is invalid");
        }
        return generateAuth(user);
    }

    public void logout(String token) {
        tokenService.blacklistAccessToken(token);
    }
}
