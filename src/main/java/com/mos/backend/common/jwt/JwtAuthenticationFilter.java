package com.mos.backend.common.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;

    public static final String[] whitelist = {
            "/users/tokens" // 토큰 재발급
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PatternMatchUtils.simpleMatch(whitelist, request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            verifyAccessToken(request);
        } catch (JWTVerificationException e1) {
            try {
                verifyRefreshToken(request, response);
            } catch (JWTVerificationException e2) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void verifyAccessToken(HttpServletRequest request) {
        String accessToken = tokenUtil.extractAccessToken(request);
        Long userId = tokenUtil.verifyAccessToken(accessToken);
        setAuthentication(userId);
    }

    private void verifyRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = tokenUtil.extractRefreshToken(request);
        Long userId = tokenUtil.verifyRefreshToken(refreshToken);
        tokenUtil.addTokenToCookie(response, userId);
        setAuthentication(userId);
    }

    private void setAuthentication(Long userId) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}



