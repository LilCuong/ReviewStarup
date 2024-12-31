package com.example.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Recharge;
import com.example.Repository.RechargeReposity;

@Service
public class RechargeService {

	@Autowired
	RechargeReposity rechargeReposity;
	
	public Optional<Recharge> findById(int id){
		return rechargeReposity.findById(id);
	}
	
	public List<Recharge> findAll(){
		return rechargeReposity.findAll();
	}
	
	public List<Recharge> findByUserId(int id){
		return rechargeReposity.findByUserId(id);
	}
	
	public void save(Recharge recharge) {
		 rechargeReposity.save(recharge);
	}
	
	public void deleteById(int id) {
		rechargeReposity.deleteById(id);
	}
	
	public void delete(Recharge recharge) {
		rechargeReposity.delete(recharge);
	}
	
	public void deleteAll(){
		 rechargeReposity.deleteAll();
	}
}
