package com.kelvin.ms_app.controller;

import com.kelvin.ms_app.entity.Expense;
import com.kelvin.ms_app.model.ApiObjectResponse;
import com.kelvin.ms_app.model.ExpenseRequest;
import com.kelvin.ms_app.model.ObjectResponse;
import com.kelvin.ms_app.model.SignUpRequest;
import com.kelvin.ms_app.service.ExpenseService;
import com.kelvin.ms_app.service.KeycloakServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/exp")
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);;

    @PostMapping("/addExp")
    public ResponseEntity<?> addExpRecord(@Valid @RequestBody ExpenseRequest expenseRequest) {

        logger.info("Received request to add expense record");
        try {
            ResponseEntity<?> ApiObjectResponse = expenseService.addExpRecord(expenseRequest);
            logger.info("Expense record added successfully");
            return ApiObjectResponse;
        } catch (Exception e) {
            logger.error("Error adding expense record", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add expense record", e);
        }
    }

    @GetMapping("/getExp")
    public ResponseEntity<?> getExpByUser(@RequestParam String username) {
        ObjectResponse<List<Expense>> res = expenseService.getExpRecordByUser(username);

        if (res.isSuccess()) {
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.badRequest().body(res);
        }
    }

    @PostMapping("/deleteExp")
    public ResponseEntity<?> deleteExpById(@RequestParam Long id, String username) {
        logger.info("Received request to delete expense record");
        try {
            ResponseEntity<?> ApiObjectResponse = expenseService.deleteExp(id,username);
            logger.info("Expense record deleted successfully");
            return ApiObjectResponse;
        } catch (Exception e) {
            logger.error("Error deleting expense record", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add expense record", e);
        }

    }

}
