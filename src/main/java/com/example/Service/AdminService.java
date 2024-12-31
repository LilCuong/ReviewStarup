package com.example.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Admin;
import com.example.Repository.AdminRepository;

@Service
public class AdminService {

	@Autowired
	AdminRepository adminRepository;
	
	public List<Admin> findAll() {
		return adminRepository.findAll();
	}
	
	public Admin findByUsername(String username){
		return adminRepository.findByUsername(username);
	}
	
	public void save (Admin admin) {
		adminRepository.save(admin);
	}
	
	public Optional<Admin> findById(int id) {
		return adminRepository.findById(id);
	}
	
	public void deleteById(int id) {
		adminRepository.deleteById(id);
	}
}
