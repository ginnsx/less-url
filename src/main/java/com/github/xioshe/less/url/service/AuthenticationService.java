package com.github.xioshe.less.url.service;


import com.github.xioshe.less.url.api.dto.AuthCommand;
import com.github.xioshe.less.url.api.dto.AuthResponse;
import com.github.xioshe.less.url.api.dto.LoginCommand;
import com.github.xioshe.less.url.api.dto.RegisterEmailCommand;
import com.github.xioshe.less.url.api.dto.SignupCommand;
import com.github.xioshe.less.url.api.dto.VerificationCommand;
import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.repository.UserRepository;
import com.github.xioshe.less.url.security.JwtTokenManager;
import com.github.xioshe.less.url.util.PasswordStrengthChecker;
import com.github.xioshe.less.url.util.constants.RedisKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
import java.util.concurrent.TimeUnit;

import static com.github.xioshe.less.url.service.VerificationType.LOGIN;
import static com.github.xioshe.less.url.service.VerificationType.REGISTER;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String ACCOUNT_CREATION = "account-creation";

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final VerificationService verificationService;
    private final EmailService emailService;
    private final RedissonClient redissonClient;

    public User signup(SignupCommand command) {
        if (!verificationService.verify(REGISTER.getValue(), command.getEmail(), command.getVerifyCode())) {
            throw new IllegalArgumentException("无效验证码");
        }
        if (PasswordStrengthChecker.checkStrength(command.getPassword()) < 3) {
            throw new IllegalArgumentException("密码强度不够，请使用字母、数字、符号组合，长度为 8-16 位");
        }
        User user = command.asUser(passwordEncoder);
        userRepository.save(user);
        // 发送通知邮件
        sendAccountCreationEmail(command.getEmail(), user.getUsername());
        // 清理验证码
        verificationService.clean(REGISTER.getValue(), command.getEmail());
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
        String accessToken = jwtTokenManager.generateAccessToken(securityUser);
        String refreshToken = refreshTokenService.generateRefreshToken(securityUser);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .expiresIn(jwtTokenManager.getAccessTokenExpiration())
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse refreshToken(String refreshToken, String accessToken) {
        boolean isValid = refreshTokenService.validateRefreshToken(refreshToken);
        if (!isValid) {
            throw new BadCredentialsException("refresh token is invalid");
        }
        String username = jwtTokenManager.extractUsername(refreshToken);
        if (!username.equals(jwtTokenManager.extractUsername(accessToken))) {
            throw new BadCredentialsException("refresh token is invalid");
        }
        UserDetails user = userDetailsService.loadUserByUsername(username);
        AuthResponse newTokens = generateAuth(user);

        // 将旧的 refreshToken 加入黑名单
        refreshTokenService.blacklistRefreshToken(refreshToken);

        return newTokens;
    }

    public void logout(String token, String refreshToken) {
        if (StringUtils.hasText(token)) {
            jwtTokenManager.blacklistAccessToken(token);
        }
        if (StringUtils.hasText(refreshToken)) {
            refreshTokenService.blacklistRefreshToken(token);
        }
    }

    public void verifyEmailBeforeRegister(RegisterEmailCommand command) {
        if (userRepository.existsByEmail(command.getEmail())) {
            throw new IllegalArgumentException("邮箱已注册");
        }
        // 每个邮箱地址锁定 1min
        RLock lock = redissonClient.getLock(RedisKeys.LOCK_PREFIX + REGISTER.getValue() + ":" + command.getEmail());
        log.info("lock name: {}", lock.getName());
        try {
            if (!lock.tryLock(0, 1, TimeUnit.MINUTES)) {
                throw new IllegalArgumentException("频繁发送验证码，请稍后再试");
            }
        } catch (InterruptedException e) {
            throw new IllegalArgumentException("频繁发送验证码，请稍后再试");
        }
        String code = verificationService.generate(REGISTER.getValue(), command.getEmail());
        sendRegisterVerificationEmail(command.getEmail(), code);
        // 不解锁，依靠锁的过期时间自动释放
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
