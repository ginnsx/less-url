package com.github.xioshe.less.url.config;

import com.github.xioshe.less.url.security.DelegatedAccessDeniedHandler;
import com.github.xioshe.less.url.security.DelegatedAuthenticationEntryPoint;
import com.github.xioshe.less.url.security.GuestAuthenticationFilter;
import com.github.xioshe.less.url.security.GuestAuthenticationProvider;
import com.github.xioshe.less.url.security.JwtAuthenticationFilter;
import com.github.xioshe.less.url.security.JwtTokenManager;
import com.github.xioshe.less.url.util.constants.RoleNames;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

    /**
     * 用于支持用户密码登录。在提供了 GuestAuthenticationProvider 时，Spring Boot 不会自动注册 DaoAuthenticationProvider。
     *
     * @return DaoAuthenticationProvider
     * @throws Exception afterPropertiesSet 抛出的异常，正常配置时可以忽略
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() throws Exception {
        DaoAuthenticationProvider provider;
        provider = new DaoAuthenticationProvider(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        provider.afterPropertiesSet();
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtTokenFilter,
                                                   GuestAuthenticationFilter guestAuthenticationFilter,
                                                   GuestAuthenticationProvider guestAuthenticationProvider,
                                                   DaoAuthenticationProvider daoAuthenticationProvider,
                                                   DelegatedAuthenticationEntryPoint entryPoint,
                                                   DelegatedAccessDeniedHandler accessDeniedHandler) throws Exception {
        return http
                .cors(Customizer.withDefaults()) // 配合注册 CorsFilter Bean 才会生效
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(manager ->
                        manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/v1/auth/**", "/s/**").permitAll()
                                .requestMatchers("/doc.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("favicon.ico").permitAll()
                                .requestMatchers("/api/v1/links/**").hasAnyRole(RoleNames.ALL_AUTHENTICATED_ROLES)
                                .requestMatchers("/api/**").authenticated()
                                .requestMatchers("/**").permitAll() // LinkRedirectInterceptor 处理重定向
                                .anyRequest().authenticated())
                .exceptionHandling(exceptionHanding ->
                        exceptionHanding.authenticationEntryPoint(entryPoint)
                                .accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(guestAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(guestAuthenticationProvider)
                .authenticationProvider(daoAuthenticationProvider)
                .build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(@Qualifier("handlerExceptionResolver")
                                                           HandlerExceptionResolver exceptionResolver) {
        return new JwtAuthenticationFilter(jwtTokenManager, userDetailsService, exceptionResolver);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 允许所有域名跨域 addAllowedOrigin() 不支持 "*"
        config.addAllowedOriginPattern("*");
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
