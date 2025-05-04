package com.poly.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySaleDTO {
 private Integer id;
 private Integer discountPercent;
 private Date createdDate;
 private Date expirationDate;
 private Integer variantProductId; 
 private String variantProductName; 
 private String color; 
 private String size; 
 private String imageUrl;
}
