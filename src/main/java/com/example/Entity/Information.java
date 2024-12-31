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
@Table(name = "informations")
public class Information {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "support_link", length = 500, nullable = true)
	private String supportLink;
	
	@Column(name = "facebook_link", length = 500, nullable = true)
	private String facebookLink;
	
	@Column(name = "contact_link", length = 500, nullable = true)
	private String ContactLink;
	
	@Column(name = "admin_name", length = 100, nullable = true)
	private String adminName;
	
	@Column(name = "call_number", length = 100, nullable = true)
	private String callNumber;
}
