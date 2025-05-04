package com.poly.dto;




import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyOrderCountDTO {
	  private Date date;
	    private Long orderCount;
}
