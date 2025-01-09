package com.kelvin.ms_app.controller;

import com.kelvin.ms_app.model.EmailRequest;
import com.kelvin.ms_app.model.ExpenseRequest;
import com.kelvin.ms_app.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/v1/email")
public class EmailController {

    @Autowired
    EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);;

    @PostMapping("/handlingEmail")
    public ResponseEntity<?> addExpRecord(@Valid @RequestBody EmailRequest request) {

        logger.info("Received request to add expense record");
        try {
            ResponseEntity<?> ObjectResponse = emailService.handleEmail(request);
            logger.info("Handling Email");
            return ObjectResponse;
        } catch (Exception e) {
            logger.error("Error Handling Email", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error Handling Email", e);
        }
    }
}
