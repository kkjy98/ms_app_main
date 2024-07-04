package com.kelvin.ms_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "ms_expense")
public class Expense {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long transc_id;
    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "username", nullable = false)
    private String username;

    // Getters and Setters
    public long getTransc_id() {
        return transc_id;
    }

    public void setTransc_id(long transc_id) {
        this.transc_id = transc_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
