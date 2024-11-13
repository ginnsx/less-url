package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.entity.auth.User;
import com.github.xioshe.less.url.security.JwtTokenManager;
import com.github.xioshe.less.url.security.RotatingSecretKeyManager;
import com.github.xioshe.less.url.security.SecurityUser;
import com.github.xioshe.less.url.util.constants.RedisKeys;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource(properties = "security.jwt.access-expiration-seconds=0")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExpiredJwtTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private RotatingSecretKeyManager keyManager;

    @Test
    void expired_jwt() throws Exception {
        var user = new User();
        user.setUsername("test");
        user.setEmail("test@lu.com");
        user.setPassword("password");
        var token = jwtTokenManager.generateAccessToken(SecurityUser.from(user));

        mockMvc.perform(get("/api/test/user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.data.description").value("The JWT token has expired"));
    }

    @Test
    void invalid_jwt() throws Exception {
        var token = Jwts.builder()
                .claims()
                .subject("test@lu.com")
                .issuedAt(new Date(System.currentTimeMillis() + 10_000))
                .notBefore(new Date(System.currentTimeMillis() + 10_000))
                .expiration(new Date(System.currentTimeMillis() + 10_000))
                .and()
                .signWith(keyManager.getCurrentKey(), Jwts.SIG.HS256)
                .compact();

        mockMvc.perform(get("/api/test/user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code" ).value(401))
                .andExpect(jsonPath("$.data.description").value("The JWT token has not become valid yet"));
    }
}

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    void setup() {
        stringRedisTemplate.delete(RedisKeys.CACHE_USER_PREFIX + "test@lu.com");
        stringRedisTemplate.delete(RedisKeys.CACHE_USER_PREFIX + "admin@lu.com");
    }


    @Test
    @WithUserDetails("test@lu.com")
    void access_successfully() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello, user"));
    }

    @Test
    @WithUserDetails("admin@lu.com")
    void admin_access_successfully() throws Exception {
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello, admin"));
    }

    @Test
    void invalid_jwt() throws Exception {
        mockMvc.perform(get("/api/test/user")
                        .header("Authorization", "Bearer invalid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code" ).value(401))
                .andExpect(jsonPath("$.data.description").value("The JWT signature is invalid"));
    }

    @Test
    void empty_jwt() throws Exception {
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code" ).value(401))
                .andExpect(jsonPath("$.data.description")
                        .value("Full authentication is required to access this resource"));
    }

    @Test
    void blacklist_jwt() throws Exception {
        var user = SecurityUser.builder()
                .email("test@lu.com")
                .password("password")
                .build();
        var token = jwtTokenManager.generateAccessToken(user);

        jwtTokenManager.blacklistAccessToken(token);

        mockMvc.perform(get("/api/test/user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code" ).value(401))
                .andExpect(jsonPath("$.data.detail")
                        .value("Token is blacklisted"));
    }

    @Test
    void not_authorized() throws Exception {
        var user = new User();
        user.setUsername("test");
        user.setEmail("test@lu.com");
        user.setPassword("password");
        var token = jwtTokenManager.generateAccessToken(SecurityUser.from(user));

        mockMvc.perform(get("/api/test/admin")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code" ).value(403))
                .andExpect(jsonPath("$.data.description")
                        .value("You are not authorized to access this resource"));
    }

    @Test
    void required_permission() throws Exception {
        var user = SecurityUser.builder()
                .email("test@lu.com")
                .password("password")
                .build();
        var token = jwtTokenManager.generateAccessToken(user);

        mockMvc.perform(get("/api/test/edit").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code" ).value(403))
                .andExpect(jsonPath("$.data.description")
                        .value("You are not authorized to access this resource"));
    }

    @Test
    void Login_bad_credentials() throws Exception {
        mockMvc.perform(post("/api/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"test@lu.com","password":"bad pwd"}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code" ).value(401))
                .andExpect(jsonPath("$.data.description")
                        .value("The username or password is incorrect"))
                .andExpect(jsonPath("$.data.detail").value("Bad credentials"));
    }

    @Test
    public void Login_successfully() throws Exception {
        mockMvc.perform(post("/api/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"test@lu.com","password":"password"}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code" ).value(1))
                .andExpect(jsonPath("$.data.access_token").exists());
    }
}
