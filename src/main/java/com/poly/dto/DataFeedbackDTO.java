package com.poly.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataFeedbackDTO {
	private Integer detailId;
	private Integer id_user;
	private Integer rating;
	private String content_feedback;
	private List<String> images;
}
