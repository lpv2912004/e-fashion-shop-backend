package com.poly.dto;

import java.util.Date;
import java.util.List;

import com.poly.model.ActivitySale;
import com.poly.model.User;
import com.poly.model.VariantProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleDTO {
    private Integer id;
    private String name_FS;
    private Date created_date;
    private int id_user; 
    private Boolean status;
    private List<ActivitySaleDTO> activitySales;

	
}
