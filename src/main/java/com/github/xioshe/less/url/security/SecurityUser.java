package com.github.xioshe.less.url.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;


@Schema(description = "用户详情")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUser implements UserDetails {

    public static final String ROLE_PREFIX = "ROLE_";

    private String email;

    private String nickname;

    private String password;

    private String username;

    @Singular
    private Set<GrantedAuthority> authorities;

    @Builder.Default
    private Boolean enabled = true;

    @Builder.Default
    private Boolean accountNonExpired = true;

    @Builder.Default
    private Boolean accountNonLocked = true;

    @Builder.Default
    private boolean credentialsNonExpired = true;
}
