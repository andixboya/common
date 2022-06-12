package com.andi.common.auth.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class UserPrincipalDto implements OAuth2User, UserDetails, User {
    private Long id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private AuthProvider authProvider;
    private String imageUrl;
    private Boolean emailVerified;
    private String name;
    private boolean accountNotExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public UserPrincipalDto() {
    }

    public UserPrincipalDto(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public UserPrincipalDto(UserPrincipal portalUser) {
        this.email = portalUser.getUsername();
        this.authorities = (Set<GrantedAuthority>) portalUser.getAuthorities();
        this.accountNotExpired = portalUser.isAccountNonExpired();
        this.accountNonLocked = portalUser.isAccountNonLocked();
        this.credentialsNonExpired = portalUser.isCredentialsNonExpired();
        this.enabled = portalUser.isEnabled();
        this.email = portalUser.getEmail();
        this.name = portalUser.getName();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        } else {
            // fallback logic if request won't work...
        }
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public String getLastName() {
        return null;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public Boolean getEmailVerified() {
        return emailVerified;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public AuthProvider getProvider() {
        return authProvider;
    }

    @Override
    public String getProviderId() {
        return authProvider.name();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNotExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static UserPrincipalDto create(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(Role.ROLE_USER.toString()));

        return new UserPrincipalDto(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public static UserPrincipalDto create(String email, String password) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(Role.ROLE_USER.toString()));

        return new UserPrincipalDto(
                null,
                email,
                password,
                authorities
        );
    }


    public static UserPrincipalDto create(User user, Map<String, Object> attributes) {
        UserPrincipalDto userPrincipal = UserPrincipalDto.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }
}
