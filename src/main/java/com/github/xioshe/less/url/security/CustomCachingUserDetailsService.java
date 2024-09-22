package com.github.xioshe.less.url.security;

import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.repository.mapper.RoleMapper;
import com.github.xioshe.less.url.repository.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@RequiredArgsConstructor
public class CustomCachingUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Override
    @Cacheable(value = "users", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }
        user.setRoles(roleMapper.listRolesByUserId(user.getId()));
        return user.asSecurityUser();
    }
}
