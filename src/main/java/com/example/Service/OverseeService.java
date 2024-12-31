package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Oversee;
import com.example.Repository.OverseeRepository;

import jakarta.annotation.PostConstruct;

@Service
public class OverseeService {

	@Autowired
	OverseeRepository overseeRepository;
	
	public Oversee findByUsername(String username) {
		return overseeRepository.findByUsername(username);
	}
}
