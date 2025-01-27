package com.reviewping.coflo.global.auth.jwt.utils;

import com.reviewping.coflo.global.error.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Component
public class JwtProvider {

    public final String secretKey;
    public final int accessExpTime;
    public final int refreshExpTime;
    public final String accessName;
    public final String refreshName;

    public JwtProvider(
            @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.access.expiration}") int accessExpTime,
            @Value("${jwt.refresh.expiration}") int refreshExpTime,
            @Value("${jwt.access.name}") String accessName,
            @Value("${jwt.refresh.name}") String refreshName) {
        this.secretKey = secretKey;
        this.accessExpTime = accessExpTime;
        this.refreshExpTime = refreshExpTime;
        this.accessName = accessName;
        this.refreshName = refreshName;
    }

    public String generateAccessToken(Map<String, Object> valueMap) {
        return generateToken(valueMap, accessExpTime);
    }

    public String generateRefreshToken(Map<String, Object> valueMap) {
        return generateToken(valueMap, refreshExpTime);
    }

    private String generateToken(Map<String, Object> valueMap, int validTime) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            return Jwts.builder()
                    .setHeader(Map.of("typ", "JWT"))
                    .setClaims(valueMap)
                    .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                    .setExpiration(
                            Date.from(ZonedDateTime.now().plusMinutes(validTime).toInstant()))
                    .signWith(key)
                    .compact();
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }
    }

    public Map<String, Object> validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException expiredJwtException) {
            throw new JwtException(ErrorCode.TOKEN_EXPIRED.getMessage());
        } catch (Exception e) {
            throw new JwtException(ErrorCode.TOKEN_UNSUPPORTED.getMessage());
        }
    }
}
