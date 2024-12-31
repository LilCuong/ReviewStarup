package com.example.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Bank;
import com.example.Repository.BankRepository;

@Service
public class BankService {

	@Autowired 
	BankRepository bankRepository;
	
	public Optional<Bank> findById(int id){
		return bankRepository.findById(id);
	}
	
	public void save(Bank bank) {
		bankRepository.save(bank);
	}
}
