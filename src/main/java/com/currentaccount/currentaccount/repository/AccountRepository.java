package com.currentaccount.currentaccount.repository;

import com.currentaccount.currentaccount.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
