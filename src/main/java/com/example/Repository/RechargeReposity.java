package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Recharge;

@Repository
public interface RechargeReposity extends JpaRepository<Recharge, Integer>{
	List<Recharge> findByUserId(int id);
}
