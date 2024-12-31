package com.example.Entity;

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
@Table(name = "bank")
public class Bank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "QR", length = 100, nullable = true)
	private String QR;
	
	@Column(name = "bank_name", length = 500, nullable = true)
	private String bankName;
	
	@Column(name = "account_name", length = 500, nullable = true)
	private String accountName;
	
	@Column(name = "account_number", length = 500, nullable = true)
	private String accountNumber;
	
	@Column(name = "transfer_content", length = 500, nullable = true)
	private String transferContent;
}
