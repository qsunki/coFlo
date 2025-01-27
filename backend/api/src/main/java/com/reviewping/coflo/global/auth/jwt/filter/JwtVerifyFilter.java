package com.reviewping.coflo.global.auth.jwt.filter;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.global.auth.jwt.utils.JwtProvider;
import com.reviewping.coflo.global.auth.oauth.service.AuthenticationService;
import com.reviewping.coflo.global.util.CookieUtil;
import com.reviewping.coflo.global.util.RedisUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtVerifyFilter extends OncePerRequestFilter {

    private static final String REFRESH_TOKEN_END_POINT = "/api/refresh-tokens";

    private final RedisUtil redisUtil;
    private final CookieUtil cookieUtil;
    private final JwtProvider jwtProvider;
    private final AuthenticationService authenticationService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.info("요청받은 URI: {}", requestURI);
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String accessToken = cookieUtil.getCookieValue(request, jwtProvider.accessName);
        String refreshToken = cookieUtil.getCookieValue(request, jwtProvider.refreshName);

        if (requestURI.equals(REFRESH_TOKEN_END_POINT) && refreshToken != null) {
            handleRefreshToken(response, refreshToken);
        } else if (accessToken != null) {
            handleAccessToken(accessToken);
        }

        filterChain.doFilter(request, response);
    }

    private void handleAccessToken(String accessToken) {
        Map<String, Object> claims = jwtProvider.validateToken(accessToken);
        authenticationService.setAuthentication(((Integer) claims.get("userId")).longValue());
    }

    private void handleRefreshToken(HttpServletResponse response, String refreshToken) {
        Map<String, Object> claims = jwtProvider.validateToken(refreshToken);
        String userId = ((Integer) claims.get("userId")).toString();
        String token = (String) redisUtil.get(userId);
        token = token.replaceAll("^\"|\"$", "");

        if (!refreshToken.equals(token)) {
            throw new JwtException(TOKEN_INVALID.getMessage());
        } else {
            redisUtil.delete(userId);
            String accessToken = jwtProvider.generateAccessToken(claims);
            refreshToken = jwtProvider.generateRefreshToken(claims);
            redisUtil.set(userId, refreshToken, jwtProvider.refreshExpTime);

            Cookie accessTokenCookie =
                    cookieUtil.createCookie(jwtProvider.accessName, accessToken, jwtProvider.accessExpTime * 60);

            Cookie refreshTokenCookie =
                    cookieUtil.createCookie(jwtProvider.refreshName, refreshToken, jwtProvider.refreshExpTime * 60);

            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
        }
    }
}
