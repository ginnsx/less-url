package com.github.xioshe.less.url.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@RequiredArgsConstructor
public class JwtTokenService implements InitializingBean {

    private static final String BLACKLIST_PREFIX = "lu:blacklist:";
    /**
     * 至少 32 字符
     */
    @Value("${security.jwt.secret-key}")
    private String secret;

    @Value("${security.jwt.expiration-seconds:86400}")
    private long jwtExpiration;

    private SecretKey secretKey;

    /**
     * 用于维护 token 黑名单，实现 jwt 失效的功能
     */
    private final StringRedisTemplate redisTemplate;

//    private final MeterRegistry meterRegistry;


    @Override
    public void afterPropertiesSet() throws Exception {
        secretKey = generateSecurityKey();
    }


    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration * 1000);
    }

    private String buildToken(Map<String, Object> extraClaims,
                              UserDetails userDetails,
                              long expiration) {
        return Jwts.builder()
                .claims()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .add(extraClaims)
                .and()
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = extractClaims(token);
        return claims.getSubject().equals(userDetails.getUsername())
               && !claims.getExpiration().before(new Date())
               && !isTokenBlacklisted(token);
    }

    public void blacklistToken(String token) {
        if (!StringUtils.hasText(token)) {
            return;
        }
        String username = extractUsername(token);
        long ttl = extractExpiration(token).getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            log.info("Token blacklisted for user: {}", username);
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + username, token, ttl, TimeUnit.MILLISECONDS);
//            meterRegistry.counter("jwt.blacklist.count").increment();
        }
    }

    private boolean isTokenBlacklisted(String token) {
        String username = extractUsername(token);
        String blacklistedToken = redisTemplate.opsForValue().get(BLACKLIST_PREFIX + username);
        return token.equals(blacklistedToken);
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey generateSecurityKey() {
        return StringUtils.hasText(secret)
                ? Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))
                : Jwts.SIG.HS256.key().build();
    }
}
