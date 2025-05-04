package com.poly.user;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.FlashSaleDTO3;
import com.poly.dto.ProductVariantDTO;
import com.poly.dto.VariantDiscountDTO;
import com.poly.dto.VoucherDTO;
import com.poly.dto.VoucherDTO2;
import com.poly.model.Category;
import com.poly.model.FlashSale;
import com.poly.model.Order;
import com.poly.model.Product;
import com.poly.repository.CategoryRepository;
import com.poly.repository.FlashSaleRepository;
import com.poly.repository.OrderDetailRepository;
import com.poly.repository.OrderRepository;
import com.poly.repository.ProductRepository;
import com.poly.repository.VoucherRepository;
import com.poly.service.FlashSaleService;
import com.poly.service.OrderService;
import com.poly.service.ProductService;
import com.poly.service.VoucherService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("user/api/products1")
public class UserProductRestController {
	@Autowired	
	ProductRepository productRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
 @Autowired
 VoucherRepository voucherRepository;
	@Autowired
	ProductService productService;
	@Autowired
	FlashSaleService flashSaleService;
	@Autowired
	FlashSaleRepository flashSaleRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
//	@GetMapping
//	public ResponseEntity<List<Product>> getAll() {
//		return ResponseEntity.ok(productRepository.findAll());
//	}
	
	@GetMapping("{id}")
	public ResponseEntity<Product> getById(@PathVariable("id") Integer id) {
		Optional<Product> optional = productRepository.findById(id);
		if(!optional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(optional.get());
	}
	
	@PostMapping()
	public ResponseEntity<Product> postProduct(@RequestBody Product product) {
		if(productRepository.existsById(product.getId())) {
			return ResponseEntity.badRequest().build();
		}
		productRepository.save(product);
		return ResponseEntity.ok(product);
	}
	
	
	@GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Integer id) {
        Optional<Category> optional = categoryRepository.findById(id);
        if(!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optional.get());
    }
    
    @GetMapping
	public ResponseEntity<List<Product>> getAll() {
	    return ResponseEntity.ok(productRepository.findAllWithActiveVariants());
	}
    
    @GetMapping("/best-selling")
    public ResponseEntity<List<Map<String, Object>>> getBestSellingProducts() {
        List<Map<String, Object>> bestSellingProducts = productService.getBestSellingProducts();
        return ResponseEntity.ok(bestSellingProducts);
    }
    
    
    

	@GetMapping("/all")
	public ResponseEntity<Map<String, Object>> getAllStatistics() {
	    Map<String, Object> statistics = new HashMap<>();

	    // Lấy thông tin sản phẩm và biến thể
	    List<ProductVariantDTO> products = productRepository.findActiveProductsWithVariants();
	    statistics.put("products", products); // Cung cấp sản phẩm và biến thể
	    
	    // Lấy thông tin voucher
	    List<VoucherDTO2> vouchers = voucherRepository.findAvailableVouchersWithType2(); // Cập nhật phương thức lấy voucher phù hợp với logic của bạn
	    statistics.put("vouchers", vouchers); // Cung cấp thông tin voucher

	    // Lấy thông tin flash sale và giảm giá
	    List<FlashSale> flashSales = flashSaleRepository.findValidFlashSales();
        List<FlashSaleDTO3> flashSaleDTOs = flashSales.stream()
            .map(this::toFlashSaleDTO3)
            .collect(Collectors.toList());

	    
	    statistics.put("flashSales", flashSaleDTOs); // Cung cấp thông tin flash sale với giảm giá

	    
	    
	    
	    return ResponseEntity.ok(statistics);
	}
	
	private FlashSaleDTO3 toFlashSaleDTO3(FlashSale flashSale) {
	    // Lọc các hoạt động giảm giá hợp lệ theo thời gian
	    List<VariantDiscountDTO> variants = flashSale.getActivitySales().stream()
	        .filter(activity -> 
	            activity.getCreated_date().before(new java.util.Date()) && // Đã bắt đầu
	            activity.getExpiration_date().after(new java.util.Date()) // Chưa kết thúc
	        )
	        .map(activity -> new VariantDiscountDTO(
	            activity.getVariantProduct().getId(),
	            activity.getDiscount_percent()))
	        .collect(Collectors.toList());

	    return new FlashSaleDTO3(
	        flashSale.getId(),
	        flashSale.getName_FS(),
	        flashSale.getStatus(),
	        flashSale.getCreated_date(),
	        variants
	    );
	}
	




	@GetMapping("/orders")
    public ResponseEntity<?> getOrdersByUserId(@RequestParam Integer userId, @RequestHeader("Authorization") String authorizationHeader) {

        // Lấy token từ header Authorization
        String token = authorizationHeader.replace("Bearer ", "");
String JWT_SECRET_KEY ="357638792F423F4428472B4B6250655368566D597133743677397A2443264629";
        try {
            // Giải mã JWT và lấy thông tin claims (được cho là chứa userId)
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY)  // Thay bằng secret key của bạn
                    .parseClaimsJws(token)
                    .getBody();

            // Lấy userId từ token (có thể là 'id_user' hoặc tên khác tùy cách mã hóa)
            Integer tokenUserId = (Integer) claims.get("id_user");

            // Kiểm tra xem userId trong token có khớp với userId trong yêu cầu không
            if (!tokenUserId.equals(userId)) {
                return ResponseEntity.status(HttpStatus.SC_FORBIDDEN)
                        .body("Bạn không có quyền truy cập đơn hàng của người dùng này.");
            }

            // Nếu khớp, lấy danh sách đơn hàng của người dùng
            List<Order> orders = orderRepository.findOrdersByUserId(userId);
            return ResponseEntity.ok(orders);

        } catch (Exception e) {
            // Nếu token không hợp lệ hoặc hết hạn
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED)
                    .body("Token không hợp lệ hoặc đã hết hạn.");
        }
    }

	@GetMapping("/{productId}/successful-orders")
	public Integer getSuccessfulOrderCount(@PathVariable Integer productId) {
		try {
			return orderDetailRepository.countSuccessfulOrdersByProductId(productId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



}












