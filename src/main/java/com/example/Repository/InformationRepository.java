package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Information;

@Repository
public interface InformationRepository extends JpaRepository<Information, Integer>{

}
