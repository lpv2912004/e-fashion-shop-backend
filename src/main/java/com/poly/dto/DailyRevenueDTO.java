package com.poly.dto;



import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyRevenueDTO {
	   private Date date;
	    private Double revenue;
}
