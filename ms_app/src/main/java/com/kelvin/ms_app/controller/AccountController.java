package com.kelvin.ms_app.controller;

import com.kelvin.ms_app.model.*;
import com.kelvin.ms_app.service.AccountService;
import com.kelvin.ms_app.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/acc")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return accountService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(@RequestBody TokenRequest request){
        return accountService.logout((request));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest){
        return accountService.signup(signUpRequest);
    }

    @PostMapping("/sign-in/refresh")
    public ResponseEntity<?> userRefreshSignIn(){

        String username = SecurityUtil.getCurrentUserUsername();
        System.out.println(username);
        ApiObjectResponse apiObjectResponse = new ApiObjectResponse();
        if (ObjectUtils.isEmpty(username)) {
            apiObjectResponse.setCode("404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiObjectResponse);
        }

        ObjectResponse<OAuth2IdpTokenBasicInfo> userObjResponse = accountService.userRefreshSignIn(username);

        if(!userObjResponse.isSuccess()) {
            String errorMessage = userObjResponse.getMessage();
            apiObjectResponse.setMessage(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiObjectResponse);
        }

        return ResponseEntity.status(HttpStatus.OK).body(userObjResponse);
    }
}
