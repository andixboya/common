package com.andi.common.auth.authorization;


import com.andi.common.auth.authorization.service.CredentialsService;
import com.andi.common.auth.authorization.service.ManualCredentialsService;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
public abstract class AbstractCredentialServiceConfig {

    @Bean
    public CredentialsService getCredentialService() {
        return new CredentialsService();
    }

    @Bean
    public ManualCredentialsService getManualCredentialService() {
        return new ManualCredentialsService();
    }
}
