package com.github.xioshe.less.url.security.annotations;


import com.github.xioshe.less.url.util.constants.CustomHeaders;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Parameter(name = CustomHeaders.GUEST_ID, in = ParameterIn.HEADER, description = "游客 id")
public @interface GuestAllowed {
}
