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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OrderDetails")
public class OrderDetail {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	 
	    private Integer quantity;
	    
	    private Double price;
	    
	    private String full_address;

	    private String name_color;
	    
	    private String name_size;
	    
	    private Integer discount_FS;
	    	    
	    private String name_FS;
	    
	    @ManyToOne
	    @JoinColumn(name = "id_order")
	    private Order order;

	    @ManyToOne
	    @JoinColumn(name = "id_variant_prod")
	    private VariantProduct variantProd;	

	    @JsonIgnore
	    @OneToMany(mappedBy = "orderDetail")
	    private List<Feedback> feedFeedbacks;
	    
	    
}
