		package com.poly.dto;
	import java.util.List;
	
	import com.poly.model.User;
	import com.poly.model.VariantProduct;
	
	import lombok.AllArgsConstructor;
	import lombok.Data;
	import lombok.Getter;
	import lombok.NoArgsConstructor;
	import lombok.Setter;
	
	@Data
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public class Item {
		private User user;
	    private VariantProduct variantProduct;
	    private int quantity;
	    
	    private String fullAddress;
	    private String nameColor;
	    private String nameSize;
	}
