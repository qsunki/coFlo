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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtProvider {

    public static String secretKey = JwtConstants.key;

    public static String generateToken(Map<String, Object> valueMap, int validTime) {
        try {
            SecretKey key =
                    Keys.hmacShaKeyFor(JwtProvider.secretKey.getBytes(StandardCharsets.UTF_8));
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

    public static Map<String, Object> validateToken(String token) {
        try {
            SecretKey key =
                    Keys.hmacShaKeyFor(JwtProvider.secretKey.getBytes(StandardCharsets.UTF_8));

            Map<String, Object> claim =
                    Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

            return claim;
        } catch (ExpiredJwtException expiredJwtException) {
            throw new JwtException(ErrorCode.TOKEN_EXPIRED.getMessage());
        } catch (Exception e) {
            throw new JwtException(ErrorCode.TOKEN_UNSUPPORTED.getMessage());
        }
    }
}
