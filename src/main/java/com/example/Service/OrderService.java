package com.example.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Order;
import com.example.Repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	
	public void save(Order order) {
		orderRepository.save(order);
	}
	
	public List<Order> findByUserId(int id){
		return orderRepository.findByUserId(id);
	}
	
	public void delete(Order order) {
		orderRepository.delete(order);
	}
	
	public List<Order> findAll(){
		return orderRepository.findAll();
	}
	
	public Optional<Order> findById(int id){
		return orderRepository.findById(id);
	}
	
	public void deleteAll() {
		orderRepository.deleteAll();
	}
	
}
