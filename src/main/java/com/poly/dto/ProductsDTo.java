package com.poly.dto;



import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poly.model.CartDetail;
import com.poly.model.Color;
import com.poly.model.FlashSale;
import com.poly.model.OrderDetail;
import com.poly.model.Product;
import com.poly.model.Size;
import com.poly.model.VariantProduct;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@NoArgsConstructor
@AllArgsConstructor
//public class ProductsDTo {
//	  private String nameProd;
//	    private String variantName;
//	    private Double price;
//	    private Long quantity;
//}
public class ProductsDTo {
	   private int productId;
	    private String nameProd;
	    private String variantName;
	    private Double price;
	    private Long quantity;
	    private Date soldDate; // Thêm trường thời gian bán hàng
}
