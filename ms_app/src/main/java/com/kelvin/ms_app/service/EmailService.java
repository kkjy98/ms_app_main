package com.kelvin.ms_app.service;

import com.kelvin.ms_app.model.EmailRequest;
import com.kelvin.ms_app.model.ObjectResponse;
import com.kelvin.ms_app.model.mailAlertObj;
import org.springframework.http.ResponseEntity;

public interface EmailService {

    public ResponseEntity<?> handleEmail(EmailRequest request);
    public ObjectResponse<?> sendEmail(mailAlertObj request);
}
