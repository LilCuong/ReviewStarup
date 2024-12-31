package com.example.Entity;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "username", length = 100, nullable = false)
	private String username;
	
	@Column(name = "password", length = 100, nullable = false)
	private String password;
	
	@Column(name = "email", length = 100, nullable = false)
	private String email;
	
	@Column(name = "role", length = 100, nullable = true)
	private String role;
	
	@Column(name = "amount", nullable = true)
	private int amount;
	
	@Column(name = "deposited", nullable = true)
	private int deposited;
	
	@Column(name = "update_date", nullable = true)
	private Date updateDate;
	
	@Column(name = "token", length = 10, nullable = true)
	private String token;
}
