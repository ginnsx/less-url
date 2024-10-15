package com.github.xioshe.less.url.config;

import com.github.xioshe.less.url.security.DelegatedAccessDeniedHandler;
import com.github.xioshe.less.url.security.DelegatedAuthenticationEntryPoint;
import com.github.xioshe.less.url.security.JwtTokenFilter;
import com.github.xioshe.less.url.security.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.method.PrePostTemplateDefaults;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@EnableWebSecurity // 用于支持覆盖 UserDetailsService PasswordEncoder SecurityFilterChain
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenManager jwtTokenManager;
    private final UserDetailsService userDetailsService;

    /**
     * 启用 BCrypt 哈希算法处理密码，避免明文存储密码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 禁用 eraseCredentialsAfterAuthentication
     *
     * @param builder AuthenticationManagerBuilder
     */
    @Autowired
    public void configure(AuthenticationManagerBuilder builder) {
        builder.eraseCredentials(false);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtTokenFilter jwtTokenFilter,
                                                   DelegatedAuthenticationEntryPoint entryPoint,
                                                   DelegatedAccessDeniedHandler accessDeniedHandler) throws Exception {
        return http
                .cors(Customizer.withDefaults()) // 配合注册 CorsFilter Bean 才会生效
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(manager ->
                        manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/auth/**", "/s/**").permitAll()
                                .requestMatchers("/doc.html", "/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/actuator/**").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(exceptionHanding ->
                        exceptionHanding.authenticationEntryPoint(entryPoint)
                                .accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(@Qualifier("handlerExceptionResolver")
                                         HandlerExceptionResolver exceptionResolver) {
        return new JwtTokenFilter(jwtTokenManager, userDetailsService, exceptionResolver);
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

    /**
     * 扩展 Method Security 的 SpEL 表达式，用来支持元注解
     */
    @Bean
    static PrePostTemplateDefaults prePostTemplateDefaults() {
        return new PrePostTemplateDefaults();
    }
}
