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
import jakarta.persistence.OneToOne;
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
@Table(name = "transaction")
public class Transaction {

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
	
	@Column(name = "detail", nullable = false)
	private String detail;
	
	@OneToOne
	@JoinColumn(name = "order_id", nullable = true)
	private Order order;
	
	@OneToOne
	@JoinColumn(name = "recharge_id", nullable = true)
	private Recharge recharge;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
}
