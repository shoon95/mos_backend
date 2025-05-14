package com.mos.backend.common.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mos.backend.common.exception.ErrorCode;
import com.mos.backend.common.exception.MosException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;
    private final MessageSource messageSource;

    public static final String[] whitelist = {
            "/oauth**", // oauth
            "/resources/**", "/favicon.ico", // resource
            "/ws-stomp**", "/ws-stomp/**", // ws
            "/studies", "/studies/*", // study
            "/studies/*/benefits", // study benefit
            "/studies/*/curriculums", "/studies/*/curriculums/*", // study curriculum
            "/studies/*/members", // study member
            "/studies/*/questions", "/studies/*/questions/*", // study question
            "/studies/*/requirements", "/studies/*/requirements/*", // study requirement
            "/studies/*/rules", "/studies/*/rules/*", // study rule
            "/study-schedules", "/studies/*/schedules", // study schedule
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PatternMatchUtils.simpleMatch(whitelist, request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokenUtil.extractAccessToken(request);

            DecodedJWT decodedJWT = tokenUtil.decodedJWT(token);
            Long id = decodedJWT.getClaim("id").asLong();
            Authentication authentication = new UsernamePasswordAuthenticationToken(id, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (MosException e) {
            handleException(response, e.getErrorCode());
        }
    }

    private void handleException(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectNode errorResponse = objectMapper.createObjectNode();
        errorResponse.put("message", errorCode.getMessage(messageSource));
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}



