package com.github.xioshe.less.url.security;

import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.repository.RoleRepository;
import com.github.xioshe.less.url.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class CustomCachingUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Cacheable(value = "users", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.info("User {} not found", username);
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }
        user.setRoles(roleRepository.listEnabledRolesByUserId(user.getId()));
        return user.asSecurityUser();
    }
}
