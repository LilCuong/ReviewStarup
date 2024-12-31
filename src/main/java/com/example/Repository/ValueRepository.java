package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Value;

@Repository
public interface ValueRepository  extends JpaRepository<Value, Integer>{

}
