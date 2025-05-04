package com.poly.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsDTO {
    private String username;
    private Long totalOrders;
    private Double totalAmount;
    private ProductDetailsDTO productDetails; 
    // Đảm bảo rằng đối tượng `ProductDetailsDTO` có thể khởi tạo trong HQL


}


