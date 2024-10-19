package com.github.xioshe.less.url.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 针对游客身份的认证信息，游客身份不需要认证，申请 guestId 即可
 */
@Getter
public class GuestAuthentication extends AbstractAuthenticationToken {
    private final String guestId;
    private final SecurityUser user;

    public GuestAuthentication(String guestId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.guestId = guestId;
        setAuthenticated(true);
        user = SecurityUser.guest(guestId);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

}