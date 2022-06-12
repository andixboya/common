package com.andi.common.auth.authorization.service;

import com.andi.common.auth.authorization.dto.AuthenticationRequest;
import com.andi.common.auth.authorization.dto.AuthenticationResponse;
import com.andi.common.auth.authorization.dto.CredentialData;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lms.auth.common.authorization.dto.AuthenticationRequest;
//import com.lms.auth.common.authorization.dto.AuthenticationResponse;
//import com.lms.auth.common.authorization.dto.CredentialData;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Slf4j
@Getter
public class ManualCredentialsService {

    @Async
    public Flux<String> authenticate(CredentialData credentialData) {
        return Flux.interval(Duration.ofMillis(1000), Duration.ofMinutes(credentialData.getRepeatIntervalMinutes()))
                .flatMap(d -> Flux.create(fluxSink -> {
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter(new ObjectMapper())));
                    AuthenticationRequest authenticationRequest = new AuthenticationRequest(credentialData.getUsername(), credentialData.getPassword(), "local");
                    HttpHeaders headers = new HttpHeaders();
                    headers.setBasicAuth(credentialData.getClientName(), credentialData.getClientPassword());
                    HttpEntity<AuthenticationRequest> request = new HttpEntity<>(authenticationRequest, headers);
                    ResponseEntity<AuthenticationResponse> result = null;
                    try {
                        result = restTemplate.postForEntity(credentialData.getAuthEndpointUrl(), request, AuthenticationResponse.class);
                        if (result.getStatusCode().is2xxSuccessful()) {
                            String accessToken = result.getBody().getAccessToken();
                            fluxSink.next(accessToken);
                        }
                        log.trace(result.toString());
                    } catch (Exception e) {
                        fluxSink.error(e);
                        log.error("Authentication error " + e.getMessage());
                    }
                }));
    }
}
