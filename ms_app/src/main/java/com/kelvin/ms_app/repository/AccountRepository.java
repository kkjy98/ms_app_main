package com.kelvin.ms_app.repository;

import com.kelvin.ms_app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    public Optional<Account> findByUsername(String username);
}
