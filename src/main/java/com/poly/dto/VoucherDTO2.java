package com.poly.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDTO2 {
	 private Integer id;
	    private String code;
	    private Integer discountValue;
	    private Date expirationDate;
	    private Integer quantity;
	    private String typeVoucherName;
}
