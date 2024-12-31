package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.OrderImage;

@Repository
public interface OrderImageRepository extends JpaRepository<OrderImage, Integer> {
	List<OrderImage> findByOrderId(int id);
}
