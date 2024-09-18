package com.github.xioshe.less.url.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class JwtTokenDecoder implements InitializingBean {

    /**
     * must be an HMAC hash string of 256 bits
     */
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-seconds}")
    private long jwtExpiration;

    private String secret;


    @Override
    public void afterPropertiesSet() throws Exception {
        secret = StringUtils.hasText(secretKey) ? secretKey : generateSecurityKey();
    }


    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
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
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = extractClaims(token);
        return claims.getSubject().equals(userDetails.getUsername())
               && !claims.getExpiration().before(new Date());
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateSecurityKey() {
        int length = 32;
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = secureRandom.generateSeed(length);
        return Encoders.BASE64.encode(keyBytes);
    }
}
