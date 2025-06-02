package com.mos.backend.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mos.backend.common.entity.TokenType;
import com.mos.backend.common.redis.RedisTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static ch.qos.logback.core.util.OptionHelper.isNullOrEmpty;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class TokenUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;
    @Value("${jwt.access.header-name}")
    private String accessHeaderName;
    @Value("${jwt.access.cookie-name}")
    private String accessCookieName;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;
    @Value("${jwt.refresh.header-name}")
    private String refreshHeaderName;
    @Value("${jwt.refresh.cookie-name}")
    private String refreshCookieName;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String BEARER = "Bearer ";

    private final RedisTokenUtil redisTokenUtil;

    public String issueAccessToken(Long memberId) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withClaim("id", memberId)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String issueRefreshToken(Long memberId) {
        String refreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withClaim("id", memberId)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
        saveRefreshToken(memberId, refreshToken);
        return refreshToken;
    }

    private void saveRefreshToken(Long memberId, String refreshToken) {
        redisTokenUtil.setRefreshTokenWithExpire(refreshToken, memberId, Duration.ofDays(refreshTokenExpirationPeriod));
    }

    public String extractAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (isNullOrEmpty(cookies))
            return Strings.EMPTY;

        Optional<Cookie> optionalCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(accessCookieName))
                .findFirst();

        return optionalCookie.map(Cookie::getValue).orElse(Strings.EMPTY);
    }

    public String extractAccessToken(StompHeaderAccessor accessor) {
        Optional<String> requestToken = Optional.ofNullable(accessor.getFirstNativeHeader(accessHeaderName))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.substring(7));

        return requestToken.orElse(null);
    }

    public String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (isNullOrEmpty(cookies))
            return Strings.EMPTY;

        Optional<Cookie> optionalCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(refreshCookieName))
                .findFirst();

        return optionalCookie.map(Cookie::getValue).orElse(Strings.EMPTY);
    }

    public Long verifyAccessToken(String accessToken) throws JWTVerificationException {
        DecodedJWT decodedJWT = verify(accessToken);
        return decodedJWT.getClaim("id").asLong();
    }

    public Long verifyRefreshToken(String refreshToken) throws JWTVerificationException {
        verify(refreshToken);
        return redisTokenUtil.getUserId(refreshToken);
    }

    private DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
    }
    public void addTokenToCookie(HttpServletResponse response, Long memberId) {
        String accessToken = issueAccessToken(memberId);
        String refreshToken = issueRefreshToken(memberId);

        addCookie(response, TokenType.ACCESS_TOKEN, accessToken, accessTokenExpirationPeriod.intValue());
        addCookie(response, TokenType.REFRESH_TOKEN, refreshToken, refreshTokenExpirationPeriod.intValue());
    }

    private void addCookie(HttpServletResponse response, TokenType tokenType, String tokenValue, int expiration) {
        String tokenName = tokenType == TokenType.ACCESS_TOKEN ? accessCookieName : refreshCookieName;

        ResponseCookie cookie = ResponseCookie.from(tokenName, tokenValue)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMillis(expiration))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
