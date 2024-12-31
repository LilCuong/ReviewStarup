package com.example.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Value;
import com.example.Repository.ValueRepository;

@Service
public class ValueService {

	@Autowired
	ValueRepository valueRepository;
	
	public Optional<Value> findId1() {
		return valueRepository.findById(1);
	}
	
	public void save(Value value) {
		valueRepository.save(value);
	}
}
