package com.kelvin.ms_app.service;

import com.kelvin.ms_app.entity.Expense;
import com.kelvin.ms_app.model.ExpenseRequest;
import com.kelvin.ms_app.model.ObjectResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface ExpenseService {
    ResponseEntity<?> addExpRecord(ExpenseRequest expenseRequest);

    ObjectResponse<List<Expense>> getExpRecordByUser(String username);

    ResponseEntity<?> deleteExp(Long id, String username);
}
