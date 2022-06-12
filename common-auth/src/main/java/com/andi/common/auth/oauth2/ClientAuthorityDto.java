package com.andi.common.auth.oauth2;


public class ClientAuthorityDto {
    String role;
    String client;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
