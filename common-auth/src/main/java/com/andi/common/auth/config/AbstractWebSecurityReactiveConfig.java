package com.andi.common.auth.config;

import com.andi.common.auth.oauth2.AdvancedTokenProvider;
import com.andi.common.auth.oauth2.AuthenticationManager;
import com.andi.common.auth.oauth2.SecurityContextReactiveRepository;
import org.springframework.context.annotation.Bean;

public abstract class AbstractWebSecurityReactiveConfig {

    @Bean
    public SecurityContextReactiveRepository getSecurityContextRepository(AdvancedTokenProvider tokenProvider) {
        return new SecurityContextReactiveRepository(tokenProvider);
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() {
        return new AuthenticationManager();
    }

}
