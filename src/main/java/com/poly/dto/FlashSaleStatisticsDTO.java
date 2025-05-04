package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashSaleStatisticsDTO {
    private String flashSaleName;
    private Long totalTimesUsed; // Tổng số lần sử dụng flash sale
    private Long totalItemsSold; // Tổng số sản phẩm đã bán
    private Integer variantProductId; // ID của variantProduct
    private Integer flashSaleId;      // ID của FlashSale
    private Integer orderId;         // ID của đơn hàng
    private String username;          // ID của người dùng
}
