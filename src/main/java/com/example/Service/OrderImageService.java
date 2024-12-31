package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.OrderImage;
import com.example.Repository.OrderImageRepository;

@Service
public class OrderImageService {

	@Autowired
	OrderImageRepository orderImageRepository;
	
	
	public List<OrderImage> findByOrderId(int id) {
		return orderImageRepository.findByOrderId(id);
	}
	
	public void save(OrderImage orderImage) {
		orderImageRepository.save(orderImage);
	}
	
	public void deleteById(int id) {
		orderImageRepository.deleteById(id);
	}
}
