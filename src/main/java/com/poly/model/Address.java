package com.poly.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Addresses")
public class Address {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    private Integer id_province;
	    private Integer id_district;
	    private Integer id_ward;	
	    private String full_address;
	    private Boolean is_default;
	    private Boolean is_deleted;
	    
//	    @JsonIgnore
//	    @OneToMany(mappedBy = "address")
//	    private List<User> users;
	    
	    @ManyToOne
	    @JoinColumn(name = "id_user", nullable = false)
	    private User user;

	    @JsonIgnore
	    @OneToMany(mappedBy = "address")
	    private List<Order> orders;
}
