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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Float delivery_fee;

	private Float total_cash;

	private Boolean accept_order;

	private String note;

	@ManyToOne
	@JoinColumn(name = "id_user")
	private User user;

	@ManyToOne
	@JoinColumn(name = "id_state")
	private State state;

	@ManyToOne
	@JoinColumn(name = "id_address")
	private Address address;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created_date = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_date = new Date();

	@ManyToOne
	@JoinColumn(name = "id_method_payment")
	private MethodPayment methodPayment;

	@JsonIgnore
	@OneToMany(mappedBy = "order")
	private List<OrderDetail> orderDetails;

	@JsonIgnore
	@OneToMany(mappedBy = "order")
	private List<Transaction> transactions;
	
	 @JsonIgnore
	 @OneToMany(mappedBy = "order")
	 List<OrderMoreVoucher>  orderMoreVouchers;

}
