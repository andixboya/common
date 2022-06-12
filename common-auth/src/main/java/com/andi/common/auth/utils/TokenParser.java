package com.andi.common.auth.utils;

//import com.lms.auth.common.oauth2.JwtAuthentication;
//import com.lms.auth.common.oauth2.TokenClaims;

import com.andi.common.auth.oauth2.JwtAuthentication;
import com.andi.common.auth.oauth2.TokenClaims;

import java.security.Principal;

public class TokenParser {

    public static String extractClientId(Principal principal){
        return String.valueOf(((JwtAuthentication) principal).getDetails().get(TokenClaims.client_id));
    }

    public static String extractUsername(Principal principal){
        return String.valueOf(((JwtAuthentication) principal).getDetails().get(TokenClaims.username));
    }

    public static String extractPrincipal(Principal principal){
        return (String) ((JwtAuthentication) principal).getPrincipal();
    }

    public static String extractEmail(Principal principal){
        return String.valueOf(((JwtAuthentication) principal).getDetails().get(TokenClaims.email));
    }

    public static String extractUserId(Principal principal){
        return String.valueOf(((JwtAuthentication) principal).getDetails().get(TokenClaims.sub));
    }
}
