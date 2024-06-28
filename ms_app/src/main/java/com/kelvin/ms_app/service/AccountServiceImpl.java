package com.kelvin.ms_app.service;

import com.kelvin.ms_app.entity.Account;
import com.kelvin.ms_app.model.*;
import com.kelvin.ms_app.repository.AccountRepository;
import com.kelvin.ms_app.util.CryptUtil;
import com.kelvin.ms_app.util.SecurityUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    KeycloakService keycloakService;
    @Autowired
    private Environment env;
    @Autowired
    @Qualifier("requestResponseLoggingInterceptor")
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", "ms_app_restapi");
        map.add("client_secret", "zFe0biM6ys52wnMSOoWgMW35q55KbGX0");
        map.add("username", loginRequest.getUsername());
        map.add("password", loginRequest.getPassword());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                "http://localhost:8080/realms/ms_app/protocol/openid-connect/token",
                httpEntity,
                LoginResponse.class
        );

        LoginResponse loginResponse = response.getBody();
        if (response.getStatusCode() == HttpStatus.OK && loginResponse != null) {
            loginResponse.setResult_code("1000");
            return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
        } else {
            if (loginResponse != null) {
                loginResponse.setResult_code("404");
            }
            return new ResponseEntity<>(loginResponse, response.getStatusCode());
        }
    }

    @Override
    public ResponseEntity<Response> logout(TokenRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("client_id", "ms_app_restapi");
        map.add("refresh_token", request.getToken());


        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<Response> response = restTemplate.postForEntity("http://localhost:8080/realms/ms_app/protocol/openid-connect/logout", httpEntity, Response.class);

        Response res = new Response();
        if (response.getStatusCode().is2xxSuccessful()) {
            res.setMessage("Logged out successfully");
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> signup(SignUpRequest signUpRequest) {
        ObjectResponse<Account> rest = new ObjectResponse<>();
        Account account = new Account();
        account.setEmail(signUpRequest.getEmail());
        account.setUsername(signUpRequest.getUsername());
        account.setPassword(CryptUtil.encrypt(signUpRequest.getPassword()));
        account.setFirstName(signUpRequest.getFirstName());
        account.setLastName(signUpRequest.getLastName());
        account.setPhoneNo(signUpRequest.getPhoneNumber());
        account.setStatus("A");

        // Save the account to the repository
        try {
            accountRepository.save(account);

            // Create response object
            ApiResponse response = new ApiResponse("Account created successfully", "success", account);
            ObjectResponse<Account> createResUserResponse = createAccountInKeycloak(signUpRequest, account,
                    accountRepository, rest);

            if (!createResUserResponse.isSuccess()) {
                logger.error("registerAccounnt - Unable to create account \n");
                rest.setSuccess(false);
                rest.setMessage("Create account failed.");
                return new ResponseEntity<>(rest, HttpStatus.CREATED);
            }
            // Return response entity with HTTP status code 201 (Created)
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            // Handle errors and create an error response
            ApiResponse response = new ApiResponse("Account creation failed", "error", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private ObjectResponse<Account> createAccountInKeycloak(SignUpRequest req,Account acc, AccountRepository accRepo, ObjectResponse<Account> objectResponse) {
        try {
            // - in keycloak, email is unique for each user, so can't set the email
            UserRepresentation keycloakResUser = new UserRepresentation();
            keycloakResUser.setEmailVerified(Boolean.TRUE);
            keycloakResUser.setUsername(req.getUsername());
            keycloakResUser.setEmail(req.getEmail());
            keycloakResUser.setFirstName(req.getFirstName());
            keycloakResUser.setLastName(req.getLastName());
            CredentialRepresentation passwordCredential = new CredentialRepresentation();
            passwordCredential.setTemporary(Boolean.FALSE);
            passwordCredential.setType(CredentialRepresentation.PASSWORD);
            passwordCredential.setValue(CryptUtil.encrypt(req.getPassword()));
            keycloakResUser.setEnabled(Boolean.TRUE);
            keycloakResUser.setCredentials(Arrays.asList(passwordCredential));



            // set keycloak custom attributes
            Map<String, List<String>> attributes = new HashMap<String, List<String>>();
            //use email as keycloak username for uniqueness
            //attributes.put("accountNo", Arrays.asList(req.getAccNo()));
            //attributes.put("accountId", Arrays.asList(String.valueOf(req.getAccId())));
            keycloakResUser.setAttributes(attributes);
            ObjectResponse<String> createKeycloakResUserObjResponse = keycloakService.createMsUser(keycloakResUser);
            //Getting user id for deletion
            String keycloakResUserId = createKeycloakResUserObjResponse.getData();
            if (!createKeycloakResUserObjResponse.isSuccess()) {
                // not success, revert it
                keycloakService.deleteMsUser(keycloakResUserId);
                accRepo.delete(acc);
                logger.error(
                        "createResUser - create keycloakResUser failed, reverted DsAccountEntity creation for username: {}",
                        acc.getUsername());

                objectResponse.setSuccess(false);
                objectResponse.setMessage("Create user failed");
                return objectResponse;
            }



            logger.info("create - keycloakResUser successfully created for username: {}, userRepresentationId = {}",
                    acc.getEmail(), keycloakResUserId);

            // set user level access to keycloak resuser's custom attribute
            ObjectResponse<UserRepresentation> keycloakResUserObjResponse = keycloakService.getMsUser(keycloakResUserId);

            // success created
            objectResponse.setSuccess(true);
            objectResponse.setMessage("created");
            objectResponse.setData(acc);

        } catch (Exception e) {
            logger.error("createAccountUser error", e);
            objectResponse.setSuccess(false);
            objectResponse.setMessage("Create user failed");
            return objectResponse;
        }

        return objectResponse;


    }



}
