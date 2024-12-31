package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Email;
import com.example.Repository.EmailRepository;

@Service
public class EmailService {

	@Autowired
	EmailRepository emailRepository;
	
	public Email getEmail() {
		return emailRepository.findById(1) .orElseThrow();
    
	}
	
	public void save(Email email) {
		 emailRepository.save(email);
	}
	
}
