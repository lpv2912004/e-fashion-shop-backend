package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.ImageFeedback;

public interface ImageFeedbackRepository extends JpaRepository<ImageFeedback, Integer>{
    List<ImageFeedback> findByFeedbackId(Integer feedbackId);
}
