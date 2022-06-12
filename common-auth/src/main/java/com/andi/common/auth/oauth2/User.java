package com.andi.common.auth.oauth2;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface User {

    Long getId();

    String getFirstName();

    String getLastName();

    String getUsername();

    String getEmail();

    String getImageUrl();

    Boolean getEmailVerified();

    String getPassword();

    AuthProvider getProvider();

    String getProviderId();

    Collection<? extends GrantedAuthority> getAuthorities();
}
