package com.github.xioshe.less.url.api.dto;

import com.github.xioshe.less.url.security.SecurityUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Data
public class UserResponse {
    private String userId;

    private String email;

    private String nickname;
    
    private Set<GrantedAuthority> authorities;
    
    public static UserResponse fromSecurityUser(SecurityUser user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setNickname(user.getNickname());
        response.setAuthorities(user.getAuthorities());
        return response;
    }
}
