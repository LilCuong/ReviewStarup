package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.StatisticHistory;
import com.example.Repository.StatisticHistoryRepository;

@Service
public class StatisticHistoryService {

	@Autowired
	StatisticHistoryRepository statisticHistoryRepository;
	
	public List<StatisticHistory> findAll(){
		return statisticHistoryRepository.findAll();
	}
	
	public void save(StatisticHistory statisticHistory) {
		statisticHistoryRepository.save(statisticHistory);
	}
	
	public StatisticHistory findById(int id) {
		return statisticHistoryRepository.findById(id).orElseThrow();
	}
	
	public void delete(StatisticHistory statisticHistory) {
		statisticHistoryRepository.delete(statisticHistory);
	}
}
