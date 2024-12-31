package com.example.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Transaction;
import com.example.Repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	TransactionRepository transactionRepository;
	
	
	public List<Transaction> findAll() {
		return transactionRepository.findAll();
	}
	
	public List<Transaction> findByUserId(int id){
		return transactionRepository.findByUserId(id);
	}
	
	public Optional<Transaction> findById(int id){
		return transactionRepository.findById(id);
	}
	
	public Optional<Transaction> findByRechargeId(int id){
		return transactionRepository.findByRechargeId(id);
	}
	
	public Optional<Transaction> findByOrderId(int id){
		return transactionRepository.findByOrderId(id);
	}
	
	
	public void save(Transaction transaction) {
		transactionRepository.save(transaction);
	}
	
	public void deleteById(int id) {
		transactionRepository.deleteById(id);
	}
	
	public void delete(Transaction transaction) {
		transactionRepository.delete(transaction);
	}
	

}
