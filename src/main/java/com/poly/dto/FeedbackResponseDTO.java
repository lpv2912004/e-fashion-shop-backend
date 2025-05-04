package com.poly.dto;

import java.util.Date;
import java.util.List;

import com.poly.model.OrderDetail;
import com.poly.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponseDTO {
	 private Integer id;
	    private Date created_date;
	    private Integer number_star;
	    private String content;
	    private User user;
	    private OrderDetail orderDetail;
	    private List<String> imageUrls;
}
