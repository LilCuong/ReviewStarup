package com.example.Entity;

import java.util.Date;

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
@Table(name = "recharge")
public class Recharge {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "prev_balance", nullable = false)
	private int prevBalance;
	
	@Column(name = "after_balance", nullable = false)
	private int afterBalance;
	
	@Column(name = "deposit_amount", nullable = false)
	private int depositAmount;
	
	@Column(name = "deposit_date", nullable = false)
	private Date depositDate;
	
	@Column(name = "transaction_code", length = 100, nullable = true)
	private String TransactionCode;
	
	@Column(name = "status", length = 100, nullable = false)
	private String status;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
}
