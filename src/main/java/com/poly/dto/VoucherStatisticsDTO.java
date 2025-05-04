package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//public class VoucherStatisticsDTO {
//    private String voucherCode;
//    private Long timesUsed;
//    private Double discount;
//}
public class VoucherStatisticsDTO {
	 private String voucherCode;
	    private Long timesUsed;
	    private Double totalDiscount;
	    private Integer orderId;
	    private String userName;
	    private String voucherType;  // Thông tin từ TypeVoucher
	    private Double condition;    // Điều kiện của Voucher
}