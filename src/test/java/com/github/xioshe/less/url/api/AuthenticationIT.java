package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.security.CustomGrantedAuthority;
import com.github.xioshe.less.url.security.JwtTokenService;
import com.github.xioshe.less.url.security.SecurityUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@TestPropertySource(properties = "security.jwt.expiration-seconds=0")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExpiredJwtIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Test
    void expired_jwt() throws Exception {
        var user = new User();
        user.setUsername("test");
        user.setPassword("password");
        var token = jwtTokenService.generateAccessToken(user.asSecurityUser());

        mockMvc.perform(get("/test/user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.description").value("The JWT token has expired"));
    }
}


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    void setup() {
        stringRedisTemplate.delete("lu:users:test");
        stringRedisTemplate.delete("lu:users:admin");
        stringRedisTemplate.delete("lu:blacklist:test");
        stringRedisTemplate.delete("lu:blacklist:admin");
    }


    @Test
    @WithUserDetails("test")
    void access_successfully() throws Exception {
        mockMvc.perform(get("/test/user"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello, user"));
    }

    @Test
    @WithUserDetails("admin")
    void admin_access_successfully() throws Exception {
        mockMvc.perform(get("/test/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello, admin"));
    }

    @Test
    void invalid_jwt() throws Exception {
        mockMvc.perform(get("/test/user")
                        .header("Authorization", "Bearer invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.description").value("The JWT signature is invalid"));
    }

    @Test
    void empty_jwt() throws Exception {
        mockMvc.perform(get("/test/user"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.description")
                        .value("Full authentication is required to access this resource"));
    }

    @Test
    void blacklist_jwt() throws Exception {
        var user = SecurityUser.builder()
                .username("test")
                .password("password")
                .authority(new CustomGrantedAuthority("USER"))
                .build();
        var token = jwtTokenService.generateAccessToken(user);

        jwtTokenService.blacklistAccessToken(token);

        mockMvc.perform(get("/test/user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.description")
                        .value("Full authentication is required to access this resource"));
    }

    @Test
    void not_authorized() throws Exception {
        var user = new User();
        user.setUsername("test");
        user.setPassword("password");
        var token = jwtTokenService.generateAccessToken(user.asSecurityUser());

        mockMvc.perform(get("/test/admin")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.description")
                        .value("You are not authorized to access this resource"));
    }

    @Test
    void Login_bad_credentials() throws Exception {
        mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"test","password":"bad pwd"}"""))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.description")
                        .value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.detail").value("Bad credentials"));
    }

    @Test
    public void Login_successfully() throws Exception {
        mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"test","password":"password"}"""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").exists());
    }
}
