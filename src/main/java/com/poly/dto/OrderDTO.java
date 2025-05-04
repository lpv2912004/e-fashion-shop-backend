package com.poly.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Integer orderId;


    private List<OrderDetailDTO> orderDetails;

    // phí và tổng tiền
    private float deliveryFee;
    private float totalCash;
    private String orderStatus;

    //người dùng và địa chỉ gh
    private String userFullName;
    private String userEmail;
    private String userPhone;
    private String shippingAddress;
    private Date createdDate;
    private String paymentMethod;

    // Mã voucher
    private Double Discount_voucher;

    private String Name_voucher;

	}

