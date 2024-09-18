package com.github.xioshe.less.url.config;

import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.repository.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@EnableWebSecurity() // 启用 WebSecurityConfiguration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    private final UserMapper userMapper;

    /**
     * 启用 BCrypt 哈希算法处理密码，避免明文存储密码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // todo cache me
        return username -> {
            User user = userMapper.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User '" + username + "' not found");
            }
            return user;
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtTokenFilter jwtTokenFilter) throws Exception {
        return http
                .cors(Customizer.withDefaults()) // 配合注册 CorsFilter Bean 才会生效
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(manager ->
                        manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/auth/**").permitAll();
                    auth.requestMatchers("/url").hasRole(ROLE_USER);
                    auth.anyRequest().authenticated();
                })
                .exceptionHandling(exceptionHanding ->
                        exceptionHanding.authenticationEntryPoint(
                                (request, response, accessDeniedException) -> {
                                    response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
                                    response.sendError(HttpStatus.UNAUTHORIZED.value(),
                                            HttpStatus.UNAUTHORIZED.getReasonPhrase());
                                }
                        ))
                .rememberMe(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(@Qualifier("handlerExceptionResolver")
                                         HandlerExceptionResolver exceptionResolver) {
        return new JwtTokenFilter(jwtTokenDecoder(), userDetailsService(), exceptionResolver);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public JwtTokenDecoder jwtTokenDecoder() {
        return new JwtTokenDecoder();
    }
}
