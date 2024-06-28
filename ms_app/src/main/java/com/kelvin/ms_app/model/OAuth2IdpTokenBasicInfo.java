package com.kelvin.ms_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OAuth2IdpTokenBasicInfo {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("accNo")
    private String accNo;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("planName")
    private String planName;

    @JsonProperty("region")
    private String region;
}
