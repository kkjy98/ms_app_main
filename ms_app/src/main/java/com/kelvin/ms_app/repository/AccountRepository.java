package com.kelvin.ms_app.repository;

import com.kelvin.ms_app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
}
