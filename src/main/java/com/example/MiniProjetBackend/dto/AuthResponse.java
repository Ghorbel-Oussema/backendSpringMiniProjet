package com.example.MiniProjetBackend.dto;

public class AuthResponse {
    public String token;
    public String role;
    public boolean enabled;

    public AuthResponse(String token, String role, boolean enabled) {
        this.token = token;
        this.role = role;
        this.enabled = enabled;
    }
}

