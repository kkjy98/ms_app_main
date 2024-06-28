package com.kelvin.ms_app.model;

import lombok.Data;

@Data
public class TokenRequest {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
}
