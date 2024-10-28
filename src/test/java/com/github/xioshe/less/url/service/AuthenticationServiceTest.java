package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.api.dto.auth.AuthCommand;
import com.github.xioshe.less.url.api.dto.auth.LoginCommand;
import com.github.xioshe.less.url.api.dto.auth.SignupCommand;
import com.github.xioshe.less.url.entity.auth.User;
import com.github.xioshe.less.url.repository.auth.UserRepository;
import com.github.xioshe.less.url.security.JwtTokenManager;
import com.github.xioshe.less.url.security.SecurityUser;
import com.github.xioshe.less.url.security.SecurityUserHelper;
import com.github.xioshe.less.url.service.auth.AuthenticationService;
import com.github.xioshe.less.url.service.auth.GuestIdService;
import com.github.xioshe.less.url.service.auth.RefreshTokenService;
import com.github.xioshe.less.url.service.auth.UserService;
import com.github.xioshe.less.url.service.auth.VerificationService;
import com.github.xioshe.less.url.service.auth.VerificationType;
import com.github.xioshe.less.url.service.common.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenManager jwtTokenManager;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private VerificationService verificationService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private EmailService emailService;
    @Mock
    private GuestIdService guestIdService;
    @Mock
    private SecurityUserHelper securityUserHelper;
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private SignupCommand signupCommand;
    private LoginCommand loginCommand;
    private AuthCommand authCommand;

    @BeforeEach
    void setUp() {
        signupCommand = new SignupCommand();
        signupCommand.setEmail("test@example.com");
        signupCommand.setPassword("@Passw0rd");
        signupCommand.setUsername("testuser");
        signupCommand.setVerifyCode("123456");

        loginCommand = new LoginCommand();
        loginCommand.setEmail("test@example.com");
        loginCommand.setPassword("@Passw0rd");

        authCommand = new AuthCommand("test@example.com", "password");
    }

    @Test
    void signup_shouldCreateNewUser() {
        when(verificationService.verify(eq(VerificationType.REGISTER.getValue()), anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User result = authenticationService.signup(signupCommand);

        assertNotNull(result);
        verify(userService).creatUser(any(User.class));
        verify(verificationService).clean(eq(VerificationType.REGISTER.getValue()), anyString());
    }

    @Test
    void login_withPassword_shouldAuthenticateUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityUser securityUser = new SecurityUser();
        securityUser.setEmail("test@example.com");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(jwtTokenManager.generateAccessToken(any(SecurityUser.class))).thenReturn("accessToken");
        when(refreshTokenService.generateRefreshToken(any())).thenReturn("refreshToken");

        var result = authenticationService.login(loginCommand);

        assertNotNull(result);
        assertEquals("accessToken", result.getAccessToken());
        assertEquals("refreshToken", result.getRefreshToken());
    }

    @Test
    void login_withVerificationCode_shouldAuthenticateUser() {
        loginCommand.setPassword(null);
        loginCommand.setVerifyCode("123456");

        when(verificationService.verify(eq(VerificationType.LOGIN.getValue()), anyString(), anyString())).thenReturn(true);
        SecurityUser securityUser = new SecurityUser();
        securityUser.setEmail("test@example.com");
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(securityUser);
        when(jwtTokenManager.generateAccessToken(any(SecurityUser.class))).thenReturn("accessToken");
        when(refreshTokenService.generateRefreshToken(any())).thenReturn("refreshToken");

        var result = authenticationService.login(loginCommand);

        assertNotNull(result);
        assertEquals("accessToken", result.getAccessToken());
        assertEquals("refreshToken", result.getRefreshToken());
        verify(verificationService).clean(eq(VerificationType.LOGIN.getValue()), anyString());
    }

    @Test
    void auth_shouldAuthenticateAndGenerateTokens() {
        Authentication authentication = mock(Authentication.class);
        SecurityUser securityUser = new SecurityUser();
        securityUser.setEmail("test@example.com");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(jwtTokenManager.generateAccessToken(any(SecurityUser.class))).thenReturn("accessToken");
        when(refreshTokenService.generateRefreshToken(any())).thenReturn("refreshToken");

        var result = authenticationService.auth(authCommand);

        assertNotNull(result);
        assertEquals("accessToken", result.getAccessToken());
        assertEquals("refreshToken", result.getRefreshToken());
    }
}