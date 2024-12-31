package com.example.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
	List<Transaction> findByUserId(int id);
	Optional<Transaction> findByRechargeId(int id);
	Optional<Transaction> findByOrderId(int id);
}
