package com.andi.common.auth.oauth2;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (authentication instanceof JwtAuthentication) {
            authentication.setAuthenticated(true);
        }
        return Mono.just(authentication);
    }
}
