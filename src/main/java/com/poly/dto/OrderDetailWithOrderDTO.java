package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class OrderDetailWithOrderDTO {
    private Integer orderDetailId;
    private Integer quantity;
    private Double price;
    private String fullAddress;
    private String nameColor;
    private String nameSize;
    private Integer discountFS;
    private String nameFS;
    
    private Float deliveryFee;
    private Float totalCash;
    private Boolean acceptOrder;
    private Integer userId;
    private Integer stateId;
    private Integer methodPaymentId;
    private Integer variantProdId;
    private Date createdDate; 
}
