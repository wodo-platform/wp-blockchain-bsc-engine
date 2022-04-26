package io.wodo.bscengine.repository;

import io.wodo.bscengine.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
