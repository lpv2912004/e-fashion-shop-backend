package com.poly.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dto.FeedbackDTO;
import com.poly.dto.FeedbackResponseDTO;
import com.poly.model.Feedback;
import com.poly.model.ImageFeedback;
import com.poly.repository.FeedbackRepository;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // Lấy tất cả các feedback và chuyển đổi sang DTO
    public List<FeedbackDTO> findAllFeedbacks() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        return feedbacks.stream()
                .map(this::convertToDTO) // Chuyển đổi mỗi feedback thành DTO
                .collect(Collectors.toList());
    }
   
    //lấy feedback và hình feedback
    public List<FeedbackResponseDTO> getFeedbacksWithImages(Integer productId) {
        List<Feedback> feedbacks = feedbackRepository.findByProductId(productId);

        return feedbacks.stream().map(feedback -> {
            List<String> imageUrls = feedback.getImageFeedbacks().stream()
                    .map(ImageFeedback::getImage_feedback)
                    .collect(Collectors.toList());

            return new FeedbackResponseDTO(
                    feedback.getId(),
                    feedback.getCreated_date(),
                    feedback.getNumber_star(),
                    feedback.getContent(),
                    feedback.getUser(),
                    feedback.getOrderDetail(),
                    imageUrls
            );
        }).collect(Collectors.toList());
    }

    // Phương thức chuyển đổi từ Feedback entity sang FeedbackDTO
    private FeedbackDTO convertToDTO(Feedback feedback) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setId(feedback.getId());
        dto.setCreatedDate(feedback.getCreated_date().toString()); // Định dạng ngày
        dto.setNumberStar(feedback.getNumber_star());
       dto.setProductName(feedback.getProduct().getName_prod()); // Chỉ lấy tên sản phẩm
        dto.setUserName(feedback.getUser().getUsername()); // Chỉ lấy tên người dùng
        dto.setContent(feedback.getContent());
        return dto;
    }

    public boolean deleteById(Long id) {
        if (feedbackRepository.existsById(id)) {
            feedbackRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
