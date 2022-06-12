package com.andi.common.auth.oauth2;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class TokenHolder {

    private final AdvancedTokenProvider advancedTokenProvider;
    private String accessToken;

    public TokenHolder(AdvancedTokenProvider advancedTokenProvider) {
        this.advancedTokenProvider = advancedTokenProvider;
    }

    public TokenHolder setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isValid() {
        return this.advancedTokenProvider.validateToken(accessToken);
    }

    public Authentication getAuthentication() {
        JwtAuthentication authentication = new JwtAuthentication();
        if (isValid()) authentication.setAuthenticated(true);
        try {
            Claims claims = this.advancedTokenProvider.getUserPrincipalClaimsFromToken(accessToken);
            //authentication.setPrincipal(String.format("%s %s", claims.get(TokenClaims.given_name), claims.get(TokenClaims.family_name)));
            Stream.of(Optional.ofNullable(claims.get(TokenClaims.username)),
                            Optional.ofNullable(claims.get(TokenClaims.email)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst()
                    .ifPresentOrElse(s -> {
                        authentication.setPrincipal(s);
                        authentication.setName((String) s);
                    }, () -> {
                        throw new RuntimeException("at least username or email token claims must be provided");
                    });
            List<Map> roles = (List<Map>) claims.get(TokenClaims.roles);
            authentication.setAuthorities(roles.stream()
                    .filter(e -> e.get("client").equals(claims.get(TokenClaims.client_id)))
                    .map(e -> new SimpleGrantedAuthority((String) e.get("role")))
                    .collect(Collectors.toSet()));
            authentication.setDetails(claims);
            authentication.setAuthenticated(true);
        } catch (ExpiredJwtException expiredJwtException) {
            log.error(expiredJwtException.getMessage());
        }
        return authentication;
    }
}
