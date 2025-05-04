package com.poly.user;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.BuyNowDTO;
import com.poly.dto.CartDetailDTO;
import com.poly.model.ActivitySale;
import com.poly.model.CartDetail;
import com.poly.model.FlashSale;
import com.poly.model.User;
import com.poly.model.VariantProduct;
import com.poly.repository.CartRepository;
import com.poly.repository.UserRepository;
import com.poly.repository.VariantProductRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("user/api/cartdetail")
public class UserCartDetailRestController {
	@Autowired
	CartRepository crps;

	@Autowired
	UserRepository urps;

	@Autowired
	VariantProductRepository vrps;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private VariantProductRepository variantProductRepository;

	@GetMapping("list")
	public ResponseEntity<List<CartDetail>> getAllCartDetail() {
		return null;
	}



	@GetMapping("{id_user}")
	public ResponseEntity<List<CartDetailDTO>> getAllCartByUser(@PathVariable("id_user") Integer id) {
	    List<CartDetail> cartDetails = crps.findAllCartDetailByIdUser(id);
	    User user = userRepository.findById(id).get();
	    if (cartDetails == null || cartDetails.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }

	    List<CartDetailDTO> cartDetailDTOs = new ArrayList<>();

	    for (CartDetail cartDetail : cartDetails) {
	        CartDetailDTO dto = new CartDetailDTO();
	        dto.setId(cartDetail.getId());
	        dto.setQuantity(cartDetail.getQuantity());
	        VariantProduct variantProduct = cartDetail.getVariantProd();
	        dto.setVariantProduct(variantProduct);
	        dto.setUser(user);
	        // Get the discount for this variant
	        Integer discount = getDiscountForVariant(variantProduct.getId());
	        dto.setDiscount(discount);

	        // Calculate the discounted price
	        Double originalPrice = variantProduct.getPrice();
	        Double discountedPrice = originalPrice - (originalPrice * discount / 100);
	        dto.setDiscountedPrice(discountedPrice);

	        cartDetailDTOs.add(dto);
	    }

	    return ResponseEntity.ok(cartDetailDTOs);
	}


	// Phương thức để lấy chiết khấu cho một biến thể sản phẩm
	private Integer getDiscountForVariant(Integer variantId) {
	    Optional<VariantProduct> variantProductOpt = vrps.findById(variantId);
	    if (variantProductOpt.isPresent()) {
	        VariantProduct variantProduct = variantProductOpt.get();
	        List<ActivitySale> activitySales = variantProduct.getActivitySales();

	        if (activitySales != null && !activitySales.isEmpty()) {
	            Date now = new Date();

	            // Find the first activity sale within the valid date range
	            ActivitySale activeSale = activitySales.stream()
	                .filter(sale -> sale.getCreated_date().before(now) && sale.getExpiration_date().after(now))
	                .max(Comparator.comparing(ActivitySale::getDiscount_percent))
	                .orElse(null);

	            if (activeSale != null) {
	                return activeSale.getDiscount_percent(); // Return the discount percent
	            }
	        }
	    }
	    return 0; // No active sale, return 0% discount
	}

	// Tính toán tổng giá trị giỏ hàng
	@GetMapping("{id_user}/total")
	public ResponseEntity<Double> getTotalCartValue(@PathVariable("id_user") Integer id) {
		List<CartDetail> cartDetails = crps.findAllCartDetailByIdUser(id);

		double total = 0.0;

		for (CartDetail cartDetail : cartDetails) {
			VariantProduct variantProduct = cartDetail.getVariantProd();
			Integer discount = getDiscountForVariant(variantProduct.getId());
			Double originalPrice = variantProduct.getPrice();
			Double discountedPrice = originalPrice - (originalPrice * discount / 100);
			total += discountedPrice * cartDetail.getQuantity();
		}

		return ResponseEntity.ok(total);
	}

	@PostMapping("/post")
	public ResponseEntity<CartDetail> postCart(@RequestBody Map<String, Object> loadData) {
		Integer id_user = (Integer) loadData.get("id_user");
		Integer id_variant = (Integer) loadData.get("id_variant");
		Integer quantity = (Integer) loadData.get("quantity");

		User user = urps.findById(id_user).orElse(null);
		VariantProduct variantProd = vrps.findById(id_variant).orElse(null);

		if (user == null || variantProd == null) {
			return ResponseEntity.badRequest().build();
		}

		CartDetail existingCartDetail = crps.findByUserIdAndVariantProdId(id_user, id_variant);

		if (existingCartDetail != null) {
			existingCartDetail.setQuantity(existingCartDetail.getQuantity() + quantity);
			return ResponseEntity.ok(crps.save(existingCartDetail));
		} else {
			CartDetail cartDetail = new CartDetail();
			cartDetail.setUser(user);
			cartDetail.setVariantProd(variantProd);
			cartDetail.setQuantity(quantity);
			return ResponseEntity.ok(crps.save(cartDetail));
		}
	}

	@DeleteMapping("delete/{id_cart}")
	public void deleteCartDetail(@PathVariable("id_cart") Integer id) {
		crps.deleteById(id);
	}


	@PostMapping("/order-direct")
	public ResponseEntity<CartDetailDTO> placeDirectOrder(@RequestBody BuyNowDTO orderRequest) {
		try {
			Integer userId = orderRequest.getId_user();
			Integer variantId = orderRequest.getId_variant();
			Integer quantity = orderRequest.getQuantity();

			// Fetch user and variant details
			User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
			VariantProduct variantProduct = variantProductRepository.findById(variantId)
					.orElseThrow(() -> new RuntimeException("Variant product not found"));

			// Create CartDetailDTO object to return to frontend
			CartDetailDTO cartDetailDTO = new CartDetailDTO();
			cartDetailDTO.setUser(user);
			cartDetailDTO.setId(variantId);
			cartDetailDTO.setVariantProduct(variantProduct);
			cartDetailDTO.setQuantity(quantity);

			// Assume discount logic and price calculation happens here
			Integer discount = getDiscountForVariant(variantProduct.getId());
			cartDetailDTO.setDiscount(discount);

			// Calculate the discounted price
			Double discountedPrice = variantProduct.getPrice() - (variantProduct.getPrice() * discount / 100);
			cartDetailDTO.setDiscountedPrice(discountedPrice);
			
			return ResponseEntity.ok(cartDetailDTO);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}


//public ResponseEntity<List<CartDetailDTO>> getAllCartByUser(@PathVariable("id_user") Integer id) {
//List<CartDetail> cartDetails = crps.findAllCartDetailByIdUser(id);
//User user = userRepository.findById(id).get();
//if (cartDetails == null || cartDetails.isEmpty()) {
//	return ResponseEntity.noContent().build();
//}
//
//List<CartDetailDTO> cartDetailDTOs = new ArrayList<>();
//
//for (CartDetail cartDetail : cartDetails) {
//	CartDetailDTO dto = new CartDetailDTO();
//	dto.setId(cartDetail.getId());
//	dto.setQuantity(cartDetail.getQuantity());
//	dto.setVariantProduct(cartDetail.getVariantProd());
//	dto.setUser(user);
//	// Lấy chiết khấu từ biến thể sản phẩm
//	VariantProduct variantProduct = cartDetail.getVariantProd();
//	Integer discount = getDiscountForVariant(variantProduct.getId());
//	dto.setDiscount(discount);
//
//	// Tính giá sau khi áp dụng chiết khấu
//	Double originalPrice = variantProduct.getPrice();
//	Double discountedPrice = originalPrice - (originalPrice * discount / 100);
//	dto.setDiscountedPrice(discountedPrice);
//
//	cartDetailDTOs.add(dto);
//}
//
//return ResponseEntity.ok(cartDetailDTOs);
//}






//private Integer getDiscountForVariant(Integer variantId) {
//Optional<VariantProduct> variantProduct = vrps.findById(variantId);
//if (variantProduct.isPresent()) {
//	FlashSale flashSale = variantProduct.get().getFlashSale();
//	if (flashSale != null && flashSale.getActivitySales() != null && !flashSale.getActivitySales().isEmpty()) {
//		ActivitySale activitySale = flashSale.getActivitySales().get(0);
//		return activitySale.getDiscount_percent();
//	}
//}
//return 0; // Nếu không có chiết khấu, trả về 0
//}




//@GetMapping("{id_user}")
//public ResponseEntity<List<CartDetail>> getAllCartByUser(@PathVariable("id_user") Integer id) {
//    List<CartDetail> cartDetails = crps.findAllCartDetailByIdUser(id);
//    if (cartDetails == null || cartDetails.isEmpty()) {
//        return ResponseEntity.noContent().build();  // Trả về 204 No Content nếu không có dữ liệu
//    }
//    return ResponseEntity.ok(cartDetails);  // Trả về danh sách chi tiết giỏ hàng
//}
