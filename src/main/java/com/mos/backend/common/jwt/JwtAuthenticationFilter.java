package com.mos.backend.common.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = tokenUtil.extractAccessToken(request);
        try {
            Optional<Long> optionalUserId = tokenUtil.verifyAccessToken(accessToken);
            optionalUserId.ifPresent(userId -> setAuthentication(userId));
        } catch (TokenExpiredException e) {
            String refreshToken = tokenUtil.extractRefreshToken(request);
            Optional<Long> optionalUserId = tokenUtil.verifyRefreshToken(refreshToken);
            optionalUserId.ifPresent(userId -> {
                tokenUtil.addTokenToCookie(response, userId);
                setAuthentication(userId);
            });
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(Long userId) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}



