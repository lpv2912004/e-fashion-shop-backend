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
@Table(name = "Vouchers")
public class Voucher {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	    
	    private String code;
	    
	    private Integer discount;

	    @Temporal(TemporalType.TIMESTAMP)
	    private Date created_date = new Date();

	    @Temporal(TemporalType.TIMESTAMP)
	    private Date expiration_date;
	    
	    private Integer quantity;
	    
	    private Boolean status;
	    
	    private String description;
	    
	    private Double condition;
	    
	    private Double max_voucher_apply = 0.0;
	    
	    @ManyToOne
	    @JoinColumn(name = "idTypeVoucher")
	    private TypeVoucher typeVoucher;
	    
	    @JsonIgnore
	    @OneToMany(mappedBy = "voucher")
	    List<OrderMoreVoucher> orderMoreVouchers;
}
