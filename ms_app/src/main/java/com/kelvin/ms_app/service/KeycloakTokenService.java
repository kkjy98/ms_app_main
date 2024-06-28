package com.kelvin.ms_app.service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;

import com.kelvin.ms_app.model.OAuth2IdpToken;
import com.kelvin.ms_app.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;



@Service
public class KeycloakTokenService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakTokenService.class);

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("keycloakRestTemplate")
    private RestTemplate keycloakRestTemplate;

    private String dssRealmInternalAccessToken;

    public String getDssRealmInternalToken() {
        if (dssRealmInternalAccessToken == null) {
            retrieveDssInternalAccessToken();
        }
        if (isExpired(dssRealmInternalAccessToken)) {
            retrieveDssInternalAccessToken();
        }
        return dssRealmInternalAccessToken;
    }

    public OAuth2IdpToken getDssRealmUserToken(String username, String password) {
        return retrieveDssUserAccessToken(username, password);
    }

    private void retrieveDssInternalAccessToken() {

        try {

            String clientId = "ms_app_restapi";
            String clientSecret = "zFe0biM6ys52wnMSOoWgMW35q55KbGX0";

            String url = "http://localhost:8080/realms/ms_app/protocol/openid-connect/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(clientId, clientSecret, Charset.forName("UTF-8"));
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
            formParameters.add(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.CLIENT_CREDENTIALS.getValue());

            HttpEntity<?> request = new HttpEntity<>(formParameters, headers);

            ResponseEntity<OAuth2IdpToken> response = keycloakRestTemplate.exchange(url, HttpMethod.POST, request, OAuth2IdpToken.class);

            String accessToken = response.getBody().getAccessToken();

            if (ObjectUtils.isEmpty(accessToken)) {
                logger.error("retrieveResInternalAccessToken - access token is empty");
                return;
            }

            dssRealmInternalAccessToken = accessToken;

        } catch (Exception e) {
            logger.error("retrieveResInternalAccessToken error", e);
        }

    }

    private OAuth2IdpToken retrieveDssUserAccessToken(String username, String password) {

        OAuth2IdpToken tokenResponse = null;

        try {

            String clientId = env.getProperty("application.partner.panel-client-id");
            String clientSecret = env.getProperty("application.partner.panel-client-secret");

            String url = env.getProperty("application.endpoints.keycloak.base-url") + "/realms/dss/protocol/openid-connect/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(clientId, clientSecret, Charset.forName("UTF-8"));
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
            formParameters.add(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.PASSWORD.getValue());
            formParameters.add(OAuth2ParameterNames.USERNAME, username);
            formParameters.add(OAuth2ParameterNames.PASSWORD, password); //keycloak store encrypted password

            HttpEntity<?> request = new HttpEntity<>(formParameters, headers);

            ResponseEntity<OAuth2IdpToken> response = keycloakRestTemplate.exchange(url, HttpMethod.POST, request, OAuth2IdpToken.class);

            String accessToken = response.getBody().getAccessToken();

            if (ObjectUtils.isEmpty(accessToken)) {
                logger.error("retrieveResUserAccessToken - access token is empty");
                return null;
            }

            tokenResponse = response.getBody();

        } catch (Exception e) {
            logger.error("resRealmUserAccessToken error", e);
        }

        return tokenResponse;

    }

    private boolean isExpired(String accessToken) {
        String[] parts = accessToken.split("\\.", 0);

        byte[] bytes = Base64.getUrlDecoder().decode(parts[1]);
        String decodedPayload = new String(bytes, StandardCharsets.UTF_8);

        HashMap<String, Object> payloadMap = JsonUtil.fromJsonToHashMap(decodedPayload);

        Instant i = Instant.ofEpochSecond(Long.valueOf((Integer) payloadMap.getOrDefault("exp", 0)));
        ZonedDateTime tokenExpiryDateTime = ZonedDateTime.ofInstant(i, ZoneId.systemDefault());

        return ZonedDateTime.now().isAfter(tokenExpiryDateTime.minusMinutes(1));
    }
}
