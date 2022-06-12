package com.andi.common.auth.oauth2;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private AdvancedTokenProvider tokenProvider;

    public JwtTokenAuthenticationFilter(AdvancedTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (tokenProvider.getEnabled()) {
            if (request.getHeader(HttpHeaders.AUTHORIZATION) != null && request.getHeader(HttpHeaders.AUTHORIZATION).startsWith("Bearer")) {
                TokenHolder tokenHolder = this.tokenProvider.getJwtFromToken(request.getHeader(HttpHeaders.AUTHORIZATION));
                if (tokenHolder.isValid()) {
                    SecurityContextHolder.getContext().setAuthentication(tokenHolder.getAuthentication());
                } else {
                    logger.info(MessageFormat.format("Invalid token|{0}|", tokenHolder.getAuthentication().getPrincipal()));
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
