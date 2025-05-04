package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyNowDTO {
	  private Integer id_user;
	    private Integer id_variant;
	    private Integer quantity;
}
