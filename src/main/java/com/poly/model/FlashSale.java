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
@Table(name = "FlashSales")
public class FlashSale {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	    
	    private String name_FS;

	    private Boolean status;
	    
	    @ManyToOne
	    @JoinColumn(name = "id_user")
	    private User user;

	    @Temporal(TemporalType.TIMESTAMP)
	    private Date created_date = new Date();

//	    @JsonIgnore
//	    @OneToMany(mappedBy = "flashSale")
//	    private List<VariantProduct> variantProds;

	    @JsonIgnore
	    @OneToMany(mappedBy = "flashSale")
	    private List<ActivitySale> activitySales;
}
