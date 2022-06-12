package com.andi.common.auth.config;

import com.andi.common.auth.exception.Oauth2InitializationException;
import com.andi.common.auth.oauth2.AdvancedTokenProvider;
import com.andi.common.auth.oauth2.JwtTokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TokenProviderConfig {

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    @Value("${app.auth.client_id:}")
    private String clientId;

    @Value("${app.auth.enabled:true}")
    private Boolean isEnabled;

    // ToDo: FIX null client
    @Bean
    public AdvancedTokenProvider getTokenProvider() {
        if (tokenSecret == null) throw new Oauth2InitializationException("app.auth.tokenSecret must be configured");
        // if (clientId == null) throw new Oauth2InitializationException("app.auth.client_id must be configured");
        return new AdvancedTokenProvider(tokenSecret, clientId, isEnabled);
    }

    @Bean
    public JwtTokenAuthenticationFilter tokenAuthenticationFilter(AdvancedTokenProvider tokenProvider) {
        return new JwtTokenAuthenticationFilter(tokenProvider);
    }

}
