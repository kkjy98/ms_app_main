package com.kelvin.ms_app.repository;

import com.kelvin.ms_app.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    public List<Expense> findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ms_expense WHERE id = :id AND username = :username", nativeQuery = true)
    int deleteExpByIdUsername(@Param("id") Long id, @Param("username") String username);
}
