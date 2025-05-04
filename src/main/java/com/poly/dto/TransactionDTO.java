package com.poly.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok tự động tạo các getter, setter, toString, equals và hashCode
@AllArgsConstructor // Tạo constructor có tất cả các tham số
@NoArgsConstructor  // Tạo constructor không tham số
public class TransactionDTO {
    private Integer id;
    private Integer orderId;
    private Float total;
    private String status;
    private String transactionCode;
    private String orderStatus;
    private String userFullName;
    private String paymentMethod;
}
