package com.poly.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
		private Integer id_address;
		private Integer id_user;
	    private Integer provinceId;
	    private Integer districtId;
	    private Integer wardId;
		private String fulladdress;	
	}
