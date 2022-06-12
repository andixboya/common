package com.andi.common.auth.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationRequest {
    String username;
    String password;
    String grant_type;
}
