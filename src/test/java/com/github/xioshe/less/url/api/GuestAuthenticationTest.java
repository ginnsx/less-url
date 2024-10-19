package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.security.JwtTokenManager;
import com.github.xioshe.less.url.security.SecurityUser;
import com.github.xioshe.less.url.service.GuestIdService;
import com.github.xioshe.less.url.util.constants.CustomHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GuestAuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private GuestIdService guestIdService;

    @Test
    void guest_http_header() throws Exception {
        var guestId =guestIdService.generateGuestId();
        mockMvc.perform(get("/test/guest")
                        .header(CustomHeaders.GUEST_ID, guestId)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(guestId));
    }

    @Test
    void prefer_jwt_to_guest() throws Exception {
        var user = SecurityUser.builder()
                .email("test@lu.com")
                .password("password")
                .build();
        var token = jwtTokenManager.generateAccessToken(user);
        var guestId =guestIdService.generateGuestId();

        mockMvc.perform(get("/test/principal")
                        .header(CustomHeaders.GUEST_ID, guestId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("test@lu.com"));
    }

    @Test
    void helper() throws Exception {
        var guestId =guestIdService.generateGuestId();

        mockMvc.perform(get("/test/helper")
                        .header(CustomHeaders.GUEST_ID, guestId))
                .andExpect(status().isOk())
                .andExpect(content().string("g_" + guestId));

        var user = SecurityUser.builder()
                .email("test@lu.com")
                .password("password")
                .build();
        var token = jwtTokenManager.generateAccessToken(user);

        mockMvc.perform(get("/test/helper")
                        .header(CustomHeaders.GUEST_ID, guestId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("u_1"));
    }
}
