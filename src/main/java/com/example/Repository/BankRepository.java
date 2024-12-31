package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer>{

}
