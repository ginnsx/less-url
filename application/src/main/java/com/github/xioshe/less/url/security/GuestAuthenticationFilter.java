package com.github.xioshe.less.url.security;

import com.github.xioshe.less.url.service.auth.GuestIdService;
import com.github.xioshe.less.url.util.constants.CustomHeaders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 构建 GuestAuthentication
 */
@Component
@RequiredArgsConstructor
public class GuestAuthenticationFilter extends OncePerRequestFilter {

    private final GuestAuthenticationProvider guestAuthenticationProvider;
    private final GuestIdService guestIdService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String guestId = request.getHeader(CustomHeaders.GUEST_ID);
        if (guestId != null
            && SecurityContextHolder.getContext().getAuthentication() == null
            && guestIdService.isValidGuestId(guestId)) {
            GuestAuthentication authentication = guestAuthenticationProvider.createGuestAuthentication(guestId);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}