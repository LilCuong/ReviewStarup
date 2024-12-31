package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Statistic;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Integer> {

}
