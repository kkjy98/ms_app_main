package com.kelvin.ms_app.service;

import com.kelvin.ms_app.model.ObjectResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;

public interface KeycloakService {

    public Boolean isResUserExists(String username);
    public ObjectResponse<String> createMsUser(UserRepresentation userRepresentation);
    public ObjectResponse<String> putPasswordForUser(CredentialRepresentation credentialRepresentation);
    public ObjectResponse<UserRepresentation> getMsUser(String userRepresentationId);
    public ObjectResponse<Void> deleteMsUser(String userRepresentationId);


}