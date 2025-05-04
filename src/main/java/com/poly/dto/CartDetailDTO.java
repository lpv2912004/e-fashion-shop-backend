package com.poly.dto;

import java.util.List;

import com.poly.model.User;
import com.poly.model.VariantProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailDTO {
	private Integer id;
	private User user;
	private Integer quantity;
	private VariantProduct variantProduct;
	private Integer discount; // Thông tin chiết khấu
	private Double discountedPrice; // Giá sau khi áp dụng chiết khấu
	
	private List<Item> items;
    private Integer totalPrice;
    private Integer deliveryFee;
    private Integer address;
    private Integer paymentMethod;
    private Integer state;
    private String vnp_TransactionStatus;  // Thêm trường trạng thái giao dịch VNPay
    private String vnp_TransactionNo; 
}
