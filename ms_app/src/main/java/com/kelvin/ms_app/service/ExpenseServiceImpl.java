package com.kelvin.ms_app.service;

import com.kelvin.ms_app.entity.Expense;
import com.kelvin.ms_app.model.ApiObjectResponse;
import com.kelvin.ms_app.model.ExpenseRequest;
import com.kelvin.ms_app.model.ObjectResponse;
import com.kelvin.ms_app.repository.ExpenseRepository;
import com.kelvin.ms_app.util.Common;
import com.kelvin.ms_app.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService{

    @Autowired
    ExpenseRepository expenseRepository;
    private static final Logger logger = LoggerFactory.getLogger(ExpenseServiceImpl.class);;

    @Override
    public ResponseEntity<?> addExpRecord(ExpenseRequest expenseRequest) {
        ApiObjectResponse apiObjectResponse = new ApiObjectResponse();
        Expense expense = new Expense();
        Common com = new Common();

        expense.setAmount(expenseRequest.getAmount());
        expense.setDate(expenseRequest.getDate());
        expense.setCategory(expenseRequest.getCategory());
        expense.setDescription(expenseRequest.getDescription());
        expense.setUsername(SecurityUtil.getCurrentUserUsername());

        try {
            expenseRepository.save(expense);
            apiObjectResponse.setMessage("Expense record added successfully.");
            apiObjectResponse.setCode("1000");
            return new ResponseEntity<>(apiObjectResponse, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            apiObjectResponse.setMessage("Failed to save expense record: " + e.getMessage());
            apiObjectResponse.setCode("404");
            return new ResponseEntity<>(apiObjectResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            apiObjectResponse.setMessage("An unexpected error occurred: " + e.getMessage());
            apiObjectResponse.setCode("404");
            return new ResponseEntity<>(apiObjectResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ObjectResponse<List<Expense>> getExpRecordByUser(String username) {

        ObjectResponse<List<Expense>> response = new ObjectResponse<>();
        try {
            List<Expense> expenseList = expenseRepository.findByUsername(username);
            response.setData(expenseList);
            response.setSubCode("1000");
            response.setSuccess(true);
            response.setMessage("Get Record success");
        }catch (Exception e) {
            logger.error("Get record error", e);
            response.setSuccess(false);
            response.setMessage("Unknown error occurred.");
            response.setSubCode("404");
        }
        return response;
    }

    @Override
    public ResponseEntity<?> deleteExp(Long id, String username) {
        ApiObjectResponse apiObjectResponse = new ApiObjectResponse();
        try{
            int row =expenseRepository.deleteExpByIdUsername(id,username);
            logger.info("Deleted row: "+row);
            apiObjectResponse.setCode("1000");
            apiObjectResponse.setMessage(id+" deleted Successfully");
            return new ResponseEntity<>(apiObjectResponse,HttpStatus.OK);
        }catch (Exception e){
            apiObjectResponse.setMessage("Error while deleting: "+ e );
            apiObjectResponse.setCode("404");
            return new ResponseEntity<>(apiObjectResponse,HttpStatus.BAD_REQUEST);
        }
    }
}
