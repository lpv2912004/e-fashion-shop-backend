package com.poly.dto;

import com.poly.model.Feedback;
import com.poly.model.OrderDetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    private Integer id;

    //sản phẩm và biến thể
    private String productName;
    private String variantName;
    private String variantSize;

    //  giá và chiết khấu
    private Double price;
    private Integer quantity;
    private Integer discount_FS;  // flash sale 
    private Integer discount_voucher;  // voucher
    private String name_FS;  // Tên Flash Sale

//    private Double discountedPrice;

    // ID của biến thể sản phẩm
    private Integer variantProdId;
    
//    private OrderDetail orderDetail;
//    private Feedback feedback;
}
