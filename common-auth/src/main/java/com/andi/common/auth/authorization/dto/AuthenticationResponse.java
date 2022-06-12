package com.andi.common.auth.authorization.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {
    String accessToken;
    String tokenType;
    String refreshToken;
    String errorMessage;
    String errorCode;
}
