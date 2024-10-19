package com.github.xioshe.less.url.security;

import com.github.xioshe.less.url.entity.Permission;
import com.github.xioshe.less.url.entity.Role;
import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.util.constants.RoleNames;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Schema(description = "用户详情")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUser implements UserDetails {

    private String userId;

    private String email;

    private String nickname;

    private String password;

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

    @Builder.Default
    private boolean isGuest = false;

    @Override
    public String getUsername() {
        return this.email;
    }

    public boolean hasRole(String role) {
        String roleAuthority = RoleNames.ROLE_NAME_PREFIX + role.toUpperCase();
        return this.authorities.stream().anyMatch(a -> a.getAuthority().equals(roleAuthority));
    }

    public static SecurityUser guest(String guestId) {
        return SecurityUser.builder()
                .userId(guestId)
                .isGuest(true)
                .build();
    }

    public static SecurityUser from(User user) {
        return SecurityUser.builder()
                .userId(String.valueOf(user.getId()))
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getUsername())
                .authorities(getAuthorities(user))
                .build();
    }

    private static Set<CustomGrantedAuthority> getAuthorities(User user) {
        Set<String> authorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            authorities.add(RoleNames.ROLE_NAME_PREFIX + role.getCode());
            for (Permission permission : role.getPermissions()) {
                authorities.add(permission.getCode());
            }
        }
        return authorities.stream().map(CustomGrantedAuthority::new).collect(Collectors.toSet());
    }
}
