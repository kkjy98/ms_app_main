package com.kelvin.ms_app.service;

import com.kelvin.ms_app.model.*;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest);
    public ResponseEntity<Response> logout(TokenRequest request);
    public ResponseEntity<?> signup(SignUpRequest signUpRequest);
}
