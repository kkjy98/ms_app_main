package com.kelvin.ms_app.model;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseRequest {


    private double amount;
    private String description;
    private String category;
    private LocalDate date;

}
