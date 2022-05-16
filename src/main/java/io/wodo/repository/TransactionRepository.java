package io.wodo.repository;

import io.wodo.domain.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.math.BigInteger;

public interface TransactionRepository extends ReactiveCrudRepository<Transaction, Long> {
}
