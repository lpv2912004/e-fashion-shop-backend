package com.poly.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDTO {
    private Integer id;
    private String code;
    private Integer discount;
    private Date expirationDate;
    private String description;
    private Double condition;
    private String typeVoucherName;
    private Double max_voucher_apply;
}

