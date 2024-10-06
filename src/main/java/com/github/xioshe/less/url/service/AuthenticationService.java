package com.github.xioshe.less.url.service;


import com.github.xioshe.less.url.api.dto.AuthCommand;
import com.github.xioshe.less.url.api.dto.AuthResponse;
import com.github.xioshe.less.url.api.dto.LoginCommand;
import com.github.xioshe.less.url.api.dto.RegisterEmailCommand;
import com.github.xioshe.less.url.api.dto.SignupCommand;
import com.github.xioshe.less.url.api.dto.VerificationCommand;
import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.repository.UserRepository;
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
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.github.xioshe.less.url.service.VerificationType.LOGIN;
import static com.github.xioshe.less.url.service.VerificationType.REGISTER;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String ACCOUNT_CREATION = "account-creation";

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final VerificationService verificationService;
    private final EmailService emailService;

    public User signup(SignupCommand command) {
        if (!verificationService.verify(REGISTER.getValue(), command.getEmail(), command.getVerifyCode())) {
            throw new IllegalArgumentException("无效验证码");
        }
        // todo 密码强度检测
        User user = command.asUser(passwordEncoder);
        userRepository.save(user);
        // 清理验证码
        verificationService.clean(REGISTER.getValue(), command.getEmail());
        sendAccountCreationEmail(command.getEmail(), user.getUsername());
        return user;
    }

    private void sendAccountCreationEmail(String to, String username) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        emailService.sendEmail(to, ACCOUNT_CREATION, variables);
    }

    public AuthResponse auth(AuthCommand command) {
        UserDetails user = authenticate(command.getEmail(), command.getPassword());
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

    public void logout(String token, String refreshToken) {
        if (StringUtils.hasText(token)) {
            tokenService.blacklistAccessToken(token);
        }
        if (StringUtils.hasText(refreshToken)) {
            tokenService.blacklistRefreshToken(token);
        }
    }

    public void verifyEmailBeforeRegister(RegisterEmailCommand command) {
        if (userRepository.existsByEmail(command.getEmail())) {
            throw new IllegalArgumentException("邮箱已注册");
        }
        // todo 1 min 分布式锁，避免重复发送
        String code = verificationService.generate(REGISTER.getValue(), command.getEmail());
        sendRegisterVerificationEmail(command.getEmail(), code);
    }

    private void sendRegisterVerificationEmail(String to, String code) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("code", code);
        variables.put("expireTime", verificationService.getExpirationMinutes());
        emailService.sendEmail(to, REGISTER.getTemplateName(), variables);
    }

    public void sendEmailVerification(VerificationCommand command) {
        if (!userRepository.existsByEmail(command.getEmail())) {
            throw new IllegalArgumentException("邮箱未注册");
        }
        String code = verificationService.generate(command.getType().getValue(), command.getEmail());
        Map<String, Object> variables = new HashMap<>();
        variables.put("code", code);
        variables.put("expireTime", verificationService.getExpirationMinutes());
        emailService.sendEmail(command.getEmail(), LOGIN.getTemplateName(), variables);
    }

    public AuthResponse login(LoginCommand command) {
        if (!command.isValid()) {
            throw new IllegalArgumentException("无效参数");
        }
        if (command.hasVerifyCode()) {
            if (!verificationService.verify(LOGIN.getValue(), command.getEmail(), command.getVerifyCode())) {
                throw new IllegalArgumentException("无效验证码");
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(command.getEmail());
            verificationService.clean(LOGIN.getValue(), command.getEmail());
            return generateAuth(userDetails);
        }
        return auth(new AuthCommand(command.getEmail(), command.getPassword()));
    }
}
