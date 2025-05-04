package com.poly.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatisticsDTO {
    private Integer orderId;
    private Double totalCash;
    private Float deliveryFee;
    private Boolean acceptOrder;
    private String note;
    private Date createdDate;
    private Date updatedDate;
    private String username;
    private String stateName;
    private String methodName;
    private Integer orderDetailId;
    private Integer quantity;
    private Double price;
    private String colorName;
    private String sizeName;
    private Integer variantProdId;
    private String productName;

 
}
