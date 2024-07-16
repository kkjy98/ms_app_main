package com.kelvin.ms_app.service;

import com.kelvin.ms_app.model.OAuth2IdpToken;
import com.kelvin.ms_app.model.ObjectResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@Service
public class KeycloakServiceImpl implements KeycloakService{

    private static final Logger logger = LoggerFactory.getLogger(KeycloakServiceImpl.class);;
    @Autowired
    private Environment env;
    @Autowired
    @Qualifier("keycloakRestTemplate")
    private RestTemplate keycloakRestTemplate;

    @Autowired
    private KeycloakTokenService keycloakTokenService;

    @Override
    public Boolean isResUserExists(String username) {
        return null;
    }

    @Override
    public ObjectResponse<String> createMsUser(UserRepresentation user) {
        ObjectResponse<String> objectResponse = new ObjectResponse<>();

        // default set emailVerified to false
        user.setEmailVerified(Boolean.TRUE);

        if(ObjectUtils.isEmpty(user.getAttributes())) {
            // set empty attributes if is null
            user.setAttributes(new HashMap<String, List<String>>());
        }

//        if(this.isResUserExists(user.getUsername())) {
//            objectResponse.setSuccess(false);
//            objectResponse.setMessage("username already exists");
//            return objectResponse;
//        }

        String url = env.getProperty("application.url.keycloak.ms_app.users");

        String accessToken = keycloakTokenService.getDssRealmInternalToken();

        if(ObjectUtils.isEmpty(accessToken)) {
            objectResponse.setSuccess(false);
            objectResponse.setMessage("Unable to get res realm internal access token");
            return objectResponse;
        }

        ResponseEntity<String> response = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);


        HttpEntity<?> request = new HttpEntity<>(user, headers);

        try {
            response = keycloakRestTemplate.exchange(url, HttpMethod.POST, request, String.class);
        }catch(RestClientResponseException e) {
            String errorResponseBody = e.getResponseBodyAsString(StandardCharsets.UTF_8);
            logger.error("createResUser exchange error, response code = {}, response body = {}", e.getRawStatusCode(), errorResponseBody);
        }catch(Exception e) {
            logger.error("createResUser exchange error", e);
        }

        if(ObjectUtils.isEmpty(response)) {
            objectResponse.setSuccess(false);
            objectResponse.setMessage("Api request to keycloak failed");
            return objectResponse;
        }

        if(response.getStatusCode().is2xxSuccessful()) {
            objectResponse.setSuccess(true);

            String location = response.getHeaders().getFirst("location");

            if(!ObjectUtils.isEmpty(location)) {
                String resUserId = location.replaceAll(".*/([^/]+)$", "$1");
                objectResponse.setData(resUserId);
            }

        }else {
            objectResponse.setSuccess(false);
            objectResponse.setMessage("Create res user request to keycloak failed");
        }

        return objectResponse;
    }

    @Override
    public ObjectResponse<String> putPasswordForUser(CredentialRepresentation credentialRepresentation) {
        //PUT /admin/realms/{realm}/users/{id}/reset-password (Keycloak 24 API)
        ObjectResponse<String> objectResponse = new ObjectResponse<>();

        return null;
    }

    @Override
    public ObjectResponse<OAuth2IdpToken> getMsUser(String username, String password) {
        ObjectResponse<OAuth2IdpToken> objectResponse = new ObjectResponse<>();

        try {

            OAuth2IdpToken tokenResponse = keycloakTokenService.getDssRealmUserToken(username, password);

            if(ObjectUtils.isEmpty(tokenResponse)) {
                objectResponse.setSuccess(false);
                objectResponse.setMessage("Unable to get res realm user access token");
                return objectResponse;
            }

            objectResponse.setSuccess(true);
            objectResponse.setData(tokenResponse);

        } catch (Exception e) {
            logger.error("getResUserToken error", e);
        }

        return objectResponse;
    }


    @Override
    public ObjectResponse<Void> deleteMsUser(String userRepresentationId) {
        ObjectResponse<Void> objectResponse = new ObjectResponse<>();

        String url = env.getProperty("application.url.keycloak.dss.user");

        String accessToken = keycloakTokenService.getDssRealmInternalToken();

        if(ObjectUtils.isEmpty(accessToken)) {
            objectResponse.setSuccess(false);
            objectResponse.setMessage("Unable to get res realm internal access token");
            return objectResponse;
        }

        ResponseEntity<String> response = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);

        try {

            HashMap<String,String> pathMap = new HashMap<>();
            pathMap.put("id", userRepresentationId);

            response = keycloakRestTemplate.exchange(url, HttpMethod.DELETE, request, String.class, pathMap);

        }catch(RestClientResponseException e) {
            String errorResponseBody = e.getResponseBodyAsString(StandardCharsets.UTF_8);
            logger.error("deleteResUser exchange error, response code = {}, response body = {}", e.getRawStatusCode(), errorResponseBody);
        }catch(Exception e) {
            logger.error("deleteResUser exchange error", e);
        }

        if(ObjectUtils.isEmpty(response)) {
            objectResponse.setSuccess(false);
            objectResponse.setMessage("Api request to keycloak failed");
            return objectResponse;
        }

        if(response.getStatusCode().is2xxSuccessful()) {
            objectResponse.setSuccess(true);
            objectResponse.setMessage("deleted");
        }else {
            objectResponse.setSuccess(false);
            objectResponse.setMessage("delete res user request to keycloak failed");
        }

        return objectResponse;
    }
}