package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.Transaction;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
	Transaction findByTransactionCode(String txnRef);
}
