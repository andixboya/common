package com.andi.common.auth.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CredentialData {

    private String clientName;

    private String clientPassword;

    private String username;

    private String password;

    private String authEndpointUrl;

    private Long repeatIntervalMinutes;
}
