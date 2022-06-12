package com.andi.common.auth.authorization.service;

import com.andi.common.auth.authorization.dto.AuthenticationRequest;
import com.andi.common.auth.authorization.dto.AuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;

@Slf4j
@Getter
public class CredentialsService {

    @Value("${app.auth.clientName:}")
    private String clientName;

    @Value("${app.auth.clientPassword:}")
    private String clientPassword;

    @Value("${app.auth.username:}")
    private String username;

    @Value("${app.auth.password:}")
    private String password;

    @Value("${app.auth.endpoint:}")
    private String endpoint;

    @Value("${app.auth.repeatIntervalMinutes:3600000}")
    private Long repeatIntervalMinutes;

    @Value("${app.auth.onStart:true}")
    private final boolean authenticateOnStart = true;

    private final RestTemplate restTemplate;

    private String accessToken;

    public CredentialsService() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter(new ObjectMapper())));
    }

    // ToDo: not working
    @PostConstruct
    public void init() {
        if (authenticateOnStart) {
            log.info("Authentication onStart enabled.");
            this.authenticate().subscribe();
        }
    }

    @Async
    public Flux<String> authenticate() {
        return Flux.interval(Duration.ofMillis(1000), Duration.ofMinutes(repeatIntervalMinutes))
                .flatMap(d -> Flux.create(fluxSink -> {
                    log.info("Authenticating ...");
                    AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password, "local");
                    HttpHeaders headers = new HttpHeaders();
                    headers.setBasicAuth(clientName, clientPassword);
                    HttpEntity<AuthenticationRequest> request = new HttpEntity<>(authenticationRequest, headers);
                    ResponseEntity<AuthenticationResponse> result = null;
                    try {
                        result = restTemplate.postForEntity(endpoint, request, AuthenticationResponse.class);
                        if (result.getStatusCode().is2xxSuccessful()) {
                            this.accessToken = result.getBody().getAccessToken();
                            log.info("Authentication successful.");
                            fluxSink.next(this.accessToken);
                        }
                        log.trace(result.toString());
                    } catch (Exception e) {
                        fluxSink.error(e);
                        log.error("Authentication error " + e.getMessage());
                    }
                }));
    }
}
