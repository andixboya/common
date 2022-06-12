package com.andi.common.auth.oauth2;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class SecurityContextReactiveRepository implements ServerSecurityContextRepository {

    private AdvancedTokenProvider advancedTokenProvider;

    public SecurityContextReactiveRepository(AdvancedTokenProvider advancedTokenProvider) {
        this.advancedTokenProvider = advancedTokenProvider;
    }

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        // Don't know yet where this is for.
        throw new UnsupportedOperationException("Not supported yet. ToDo!");
    }

    // ToDo: PreAuthorize not working
    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        HttpHeaders headers = serverWebExchange.getRequest().getHeaders();
        if (headers.containsKey(HttpHeaders.AUTHORIZATION) && this.advancedTokenProvider.getEnabled()) {
            TokenHolder tokenHolder = this.advancedTokenProvider.getJwtFromToken(headers.get(HttpHeaders.AUTHORIZATION).get(0));
            if (tokenHolder.isValid()) {
                return Mono.just(new SecurityContextImpl(tokenHolder.getAuthentication()));
            }
        }
        return Mono.empty();
    }
}
