package com.poly.dto;

import com.poly.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private String username;
	private String password;
	private String Image_user;
	private int Id_user;
	private String Full_name;

}