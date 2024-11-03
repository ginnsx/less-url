package com.github.xioshe.less.url.security;

import com.github.xioshe.less.url.util.constants.RoleNames;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 在认证时支持游客类型的身份
 */
@Component
public class GuestAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof GuestAuthentication) {
            return authentication;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return GuestAuthentication.class.isAssignableFrom(authentication);
    }

    public GuestAuthentication createGuestAuthentication(String guestId) {
        return new GuestAuthentication(guestId,
                Collections.singletonList(new CustomGrantedAuthority(RoleNames.ROLE_GUEST_NAME)));
    }
}