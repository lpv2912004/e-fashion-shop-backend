package com.poly.user;

import com.poly.dto.OrderDetailDTO;
import com.poly.model.ActivitySale;
import com.poly.model.Feedback;
import com.poly.model.FlashSale;
import com.poly.model.ImageFeedback;
import com.poly.model.OrderDetail;
import com.poly.model.VariantProduct;
import com.poly.repository.FeedbackRepository;
import com.poly.repository.ImageFeedbackRepository;
import com.poly.repository.OrderDetailRepository;
import com.poly.repository.OrderRepository;
import com.poly.repository.VariantProductRepository;
import com.poly.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("user/api/order-details")
@CrossOrigin(origins = "*")
public class UserOrderDetailRestController {

	@Autowired
	private OrderDetailService orderDetailService;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	VariantProductRepository variantProductRepository;

	@Autowired
	FeedbackRepository feedbackRepository;

	@GetMapping
	public List<OrderDetail> getAllOrderDetails() {
		return orderDetailService.findAll();
	}
	
	@Autowired
	ImageFeedbackRepository imageFeedbackRepository;

	@GetMapping("/{id}")
	public ResponseEntity<OrderDetail> getOrderDetailById(@PathVariable Integer id) {
		Optional<OrderDetail> orderDetail = orderDetailService.findById(id);
		if (orderDetail.isPresent()) {
			return ResponseEntity.ok(orderDetail.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<List<OrderDetail>> getOrderDetailsByOrderId(@PathVariable("orderId") Integer orderId) {
		List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
		return ResponseEntity.ok(orderDetails);
	}

//	    @GetMapping("/orderDetail/{idOrder}")
//	    public ResponseEntity<List<OrderDetail>> metMethodName(@PathVariable("idOrder") Integer id) {
//	        List<OrderDetail> list = orderDetailRepository.findbyOrderId(id);
//	        return ResponseEntity.ok(list);
//	    }
	@GetMapping("/orderDetail/{idOrder}")
	public ResponseEntity<?> getOrderDetailsWithDiscount(@PathVariable("idOrder") Integer id) {
		List<OrderDetail> orderDetails = orderDetailRepository.findbyOrderId(id);
		return ResponseEntity.ok(orderDetails);
	}

//        orderDetails.forEach(orderDetail -> {
//        orderDetail.setFeedFeedbacks(feedbackRepository.findByOrderDetailId(orderDetail.getId()));
//    });

	@GetMapping("orderDetailWithFeedback")
	public ResponseEntity<?> getAllFeedbackEntity() {
		List<Feedback> list = feedbackRepository.findAll();
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/orderDetailWithFeedbackAndImages")
	public ResponseEntity<?> getFeedbackWithImages() {
	    List<Feedback> feedbacks = feedbackRepository.findAll();
	    List<Map<String, Object>> result = new ArrayList<>();
	    for (Feedback feedback : feedbacks) {
	        Map<String, Object> feedbackData = new HashMap<>();
	        feedbackData.put("id", feedback.getId());
	        feedbackData.put("number_star", feedback.getNumber_star());
	        feedbackData.put("content", feedback.getContent());
	        feedbackData.put("orderDetailId", feedback.getOrderDetail().getId());
	        feedbackData.put("images", imageFeedbackRepository.findByFeedbackId(feedback.getId())
	                .stream()
	                .map(ImageFeedback::getImage_feedback)
	                .collect(Collectors.toList()));
	        result.add(feedbackData);
	    }
	    return ResponseEntity.ok(result);
	}

	private Integer getDiscountForVariant(Integer variantId) {
		Optional<VariantProduct> variantProduct = variantProductRepository.findById(variantId);
//	        if (variantProduct.isPresent()) {
//	            FlashSale flashSale = variantProduct.get().getFlashSale();
//	            if (flashSale != null && flashSale.getActivitySales() != null && !flashSale.getActivitySales().isEmpty()) {
//	                ActivitySale activitySale = flashSale.getActivitySales().get(0);
//	                return activitySale.getDiscount_percent();
//	            }
//	        }
		return 0; // Return 0 if no discount
	}

//        List<OrderDetailDTO> orderDetailDTOs = new ArrayList<>();
//        for (OrderDetail orderDetail : orderDetails) {
//        OrderDetailDTO dto = new OrderDetailDTO();
//        dto.setId(orderDetail.getId());
//        dto.setQuantity(orderDetail.getQuantity());
//        VariantProduct variantProduct = orderDetail.getVariantProd();
//
//        // Fetch and apply discount
//        Integer discount = getDiscountForVariant(variantProduct.getId());
//        dto.setDiscount(discount);
//
//        Double originalPrice = variantProduct.getPrice();
//        Double discountedPrice = originalPrice - (originalPrice * discount / 100);
//        dto.setDiscountedPrice(discountedPrice);
//
//        dto.setVariantProduct(variantProduct);
//        dto.setOrder(orderDetail.getOrder());
//
//        orderDetailDTOs.add(dto);
//    }
}
