package com.andi.common.auth.oauth2;

import com.andi.common.auth.exception.Oauth2InitializationException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


public class AdvancedTokenProvider {

    private final String tokenSecret;

    private final String clientId;

    private Boolean isEnabled;

    private static final Logger logger = LoggerFactory.getLogger(AdvancedTokenProvider.class);

    public AdvancedTokenProvider(String tokenSecret, String clientId, Boolean isEnabled) {
        this.tokenSecret = tokenSecret;
        this.clientId = clientId;
        this.isEnabled = isEnabled;
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(getUserPrincipalClaimsFromToken(token).getSubject());
    }

    public TokenHolder getTokenHolder(String token) {
        return new TokenHolder(this).setAccessToken(token);
    }

    public List<String> getAuthorities(String token) {
        List<String> authorities = new ArrayList<>();
        getUserPrincipalClaimsFromToken(token).get(TokenClaims.roles);
        return authorities;
    }

    public Claims getUserPrincipalClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(authToken);
            if (StringUtils.isEmpty(clientId)) {
                return true;
            }
            String tokenClientId = claims.getBody().get("client_id").toString();
            return tokenClientId.equals(clientId);
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return getJwtFromToken(bearerToken).getAccessToken();
    }

    public TokenHolder getJwtFromToken(String token) {
        if (token != null && token.length() > 0) {
            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                return new TokenHolder(this).setAccessToken(token.substring(7));
            }
            return null;
        }
        throw new Oauth2InitializationException("Invalid token");
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}
