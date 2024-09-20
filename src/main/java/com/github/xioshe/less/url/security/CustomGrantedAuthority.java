package com.github.xioshe.less.url.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;


@Data
@NoArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {

    private String role;

    public CustomGrantedAuthority(String role) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return this.role;
    }
}
