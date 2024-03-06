package com.effective_mobile.test_project.repository;

import com.effective_mobile.test_project.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
