package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Oversee;

@Repository
public interface OverseeRepository extends JpaRepository<Oversee, Integer> {
boolean existsByUsername(String username);
Oversee findByUsername(String username);
}
