package com.example.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Information;
import com.example.Repository.InformationRepository;

@Service
public class InformationService {

	@Autowired
	InformationRepository informationRepository;
	
	public Optional<Information> findById(int id) {
		return informationRepository.findById(id);
	}
	
	public void save(Information information) {
		informationRepository.save(information);
	}
}
