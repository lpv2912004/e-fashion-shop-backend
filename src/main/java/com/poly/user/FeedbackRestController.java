package com.poly.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.DataFeedbackDTO;
import com.poly.dto.FeedbackResponseDTO;
import com.poly.model.Feedback;
import com.poly.model.ImageFeedback;
import com.poly.model.Order;
import com.poly.model.OrderDetail;
import com.poly.model.Product;
import com.poly.model.User;
import com.poly.model.VariantProduct;
import com.poly.repository.FeedbackRepository;
import com.poly.repository.ImageFeedbackRepository;
import com.poly.repository.OrderDetailRepository;
import com.poly.repository.OrderRepository;
import com.poly.repository.UserRepository;
import com.poly.repository.VariantProductRepository;
import com.poly.service.FeedbackService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("user/api/feedback")
public class FeedbackRestController {
	@Autowired
	FeedbackRepository feedbackRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	VariantProductRepository variantProductRepository;
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ImageFeedbackRepository imageFeedbackRepository;
	
	@Autowired
	FeedbackService feedbackService;
	
//	@GetMapping("checkexistfeedback/{id}")
//	public ResponseEntity<?> getCheckExistFeedback(@PathVariable Integer id){
//		
//	}

	@PostMapping("create")
	public ResponseEntity<?> createFeedback(@RequestBody DataFeedbackDTO dataFeedbackDTO) {
		
			System.out.println(dataFeedbackDTO);
			OrderDetail orderDetail = orderDetailRepository.findById(dataFeedbackDTO.getDetailId()).get();
			VariantProduct variantProduct = orderDetail.getVariantProd();
			Product product = variantProduct.getProduct();
			User user = userRepository.findById(dataFeedbackDTO.getId_user()).get();
			Feedback feedback = new Feedback();
			
			feedback.setNumber_star(dataFeedbackDTO.getRating());
			feedback.setProduct(product);
			feedback.setUser(user);
			feedback.setOrderDetail(orderDetail);
			feedback.setContent(dataFeedbackDTO.getContent_feedback());
			feedbackRepository.save(feedback);
			
			 // Tạo và lưu các ImageFeedback
		    if (dataFeedbackDTO.getImages() != null && !dataFeedbackDTO.getImages().isEmpty()) {
		        List<ImageFeedback> imageFeedbacks = dataFeedbackDTO.getImages().stream()
		                .map(imageUrl -> {
		                    ImageFeedback imageFeedback = new ImageFeedback();
		                    imageFeedback.setImage_feedback(imageUrl);
		                    imageFeedback.setFeedback(feedback); // Liên kết với Feedback đã lưu
		                    return imageFeedback;
		                })
		                .collect(Collectors.toList());
		        imageFeedbackRepository.saveAll(imageFeedbacks); // Lưu toàn bộ danh sách ImageFeedback
		    }
			
			return ResponseEntity.ok(null);
	

	}

	 @GetMapping("/listFeedback/{productId}")
	    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackWithImages(@PathVariable Integer productId) {
	        List<FeedbackResponseDTO> feedbacks = feedbackService.getFeedbacksWithImages(productId);
	        return ResponseEntity.ok(feedbacks);
	    }
	
	@GetMapping("list/{id}")
	public ResponseEntity<?> getFeedbackProduct(@PathVariable Integer id) {	
		List<Feedback> list = feedbackRepository.findByProductId(id);
		return ResponseEntity.ok(list);
	}
	
}