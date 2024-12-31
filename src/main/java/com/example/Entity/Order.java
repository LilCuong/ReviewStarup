package com.example.Entity;


import java.util.Date;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "map_link", length = 500, nullable = false)
	private String mapLink;
	
	@Column(name = "description", length = 500, nullable = true)
	private String description;
	
	@Column(name = "quantity", nullable = false)
	private int quantity;
	
	@Column(name = "note", nullable = true)
	private String note;
	
	@Column(name = "status", length = 100, nullable = false)
	private String status;
	
	@Column(name = "unit_price", nullable = false)
	private int unitPrice;
	
	@Column(name = "creation_date", nullable = false)
	private Date creationDate;
	
	@Column(name = "completion_date", nullable = true)
	private Date completionDate;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
}
