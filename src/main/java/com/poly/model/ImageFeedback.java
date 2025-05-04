package com.poly.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ImageFeedbacks")
public class ImageFeedback {
	 @Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id;
	 
	 private String image_feedback;
	 
	 @ManyToOne
	 @JoinColumn(name = "id_feedback")
	 private Feedback feedback;
}
