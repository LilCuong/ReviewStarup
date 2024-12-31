package com.example.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Statistic;
import com.example.Repository.StatisticRepository;

@Service
public class StatisticService {

	@Autowired
	StatisticRepository statisticRepository;
	
	public Optional<Statistic> findById(int id){
		return statisticRepository.findById(id);
	}
	
	public void save(Statistic statistic) {
		statisticRepository.save(statistic);
	}
}
