	package com.poly.model;

import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "Users")
public class User {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	    
	    private String username;
	    private String password;
	    private String fullName;
	    private boolean gender;
//	    private String email;
//
//	    @Temporal(TemporalType.DATE)
//	    private Date birthday;
//	    
//	    private String phone;
	    @Email
	    private String email;

	    @Temporal(TemporalType.DATE)
	    private Date birthday;
	    
		@Pattern(regexp = "^\\d{10}$", message = "Số điện thoại phải đủ 10 số")
	    private String phone;

	    @JsonIgnore
	    @OneToMany(mappedBy = "user")
	    private List<Address> addresses;
	    
//	    @ManyToOne
//	    @JoinColumn(name = "id_address")
//	    private Address address;	
	    
	    @Temporal(TemporalType.DATE)
	    private Date created_date = new Date();

	    @ManyToOne
	    @JoinColumn(name = "id_role")
	    private Role role;

	    private String image_user;
	
	    private boolean status_user;
	    
	    private String token_device;

	    @JsonIgnore
	    @OneToMany(mappedBy = "user")
	    private List<FlashSale> flashSales;

	    @JsonIgnore
	    @OneToMany(mappedBy = "user")
	    private List<Order> orders;

	    @JsonIgnore
	    @OneToMany(mappedBy = "user")
	    private List<Like> likes;

	    @JsonIgnore
	    @OneToMany(mappedBy = "user")
	    private List<Feedback> feedbacks;

	    @JsonIgnore
	    @OneToMany(mappedBy = "user")
	    private List<CartDetail> cartDetails;

	    @JsonIgnore
	    @OneToMany(mappedBy = "user")
	    private List<Slide> slides;

	    @JsonIgnore
	    @OneToMany(mappedBy = "user")
	    private List<Notification> notifications;
}
