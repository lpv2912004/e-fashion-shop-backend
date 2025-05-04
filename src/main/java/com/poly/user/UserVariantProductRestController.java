package com.poly.user;



import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.PriceRangeDTO;
import com.poly.dto.ProductPriceRangeDTO;
import com.poly.dto.ProductSaleDTO;
import com.poly.model.ActivitySale;
import com.poly.model.FlashSale;
import com.poly.model.VariantProduct;
import com.poly.repository.ActivityFlashSaleRepository;
import com.poly.repository.ProductRepository;
import com.poly.repository.VariantProductRepository;
@CrossOrigin(origins =  "*")
@RestController
@RequestMapping("user/api/variants")
public class UserVariantProductRestController {
	@Autowired
	VariantProductRepository vpr;
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ActivityFlashSaleRepository activityFlashSaleRepository;
	
	@Autowired
	VariantProductRepository variantProductRepository;

	@GetMapping
	public ResponseEntity<List<VariantProduct>> getAll() {
		return ResponseEntity.ok(vpr.findAll());
	}

	@GetMapping("{id}")
	public ResponseEntity<List<VariantProduct>> getAllVariantByProductId(@PathVariable("id") Integer id) {
		return ResponseEntity.ok(vpr.takeAll(id));
	}
	
	@GetMapping("singlevariant/{id_prod}")
	public ResponseEntity<VariantProduct> getVariantByProductId(@PathVariable("id_prod") Integer id_p) {
		Optional<VariantProduct> v = vpr.findById(id_p);
		return ResponseEntity.ok(v.get());
	}
	
	@GetMapping("/onsale")
	public ResponseEntity<List<ProductSaleDTO>> getProductsOnSale() {
	    List<ProductSaleDTO> saleProducts = vpr.findProductsOnSale();
	    return ResponseEntity.ok(saleProducts);
	}
	
	// Lấy thông tin chiết khấu từ ActivitySale dựa vào id biến thể
//    @GetMapping("/discount/{id}")
//    public ResponseEntity<Integer> getDiscountByVariantId(@PathVariable("id") Integer variantId) {
//        Optional<VariantProduct> variantProduct = vpr.findById(variantId);

//        if (variantProduct.isPresent()) {
//            // Lấy FlashSale từ VariantProduct
//            FlashSale flashSale = variantProduct.get().getFlashSale();
//
//            // Kiểm tra xem FlashSale có tồn tại và có ActivitySale hay không
//            if (flashSale != null && flashSale.getActivitySales() != null && !flashSale.getActivitySales().isEmpty()) {
//                // Giả định rằng chỉ có một ActivitySale, bạn có thể điều chỉnh nếu cần
//                ActivitySale activitySale = flashSale.getActivitySales().get(0);
//                Integer discountPercent = activitySale.getDiscount_percent();
//                return ResponseEntity.ok(discountPercent);
//            }
//        }
        
        //return ResponseEntity.ok(0); // Nếu không có chiết khấu, trả về 0
//    }
	
	  @GetMapping("/discount/{variantId}")
	    public ResponseEntity<Integer> getVariantDiscount(@PathVariable Integer variantId) {
		  System.out.println(variantId);
	         //Truy vấn trực tiếp từ repository để lấy chiết khấu đang hoạt động
	        Optional<ActivitySale> activitySale = activityFlashSaleRepository
	            .findActiveDiscountByVariantId(variantId);
	        
	        // Kiểm tra nếu có chiết khấu đang hoạt động thì trả về discount_percent, nếu không thì trả về 0
	        if (activitySale.isPresent()) {
	            return ResponseEntity.ok(activitySale.get().getDiscount_percent());
	        } else {
	            return ResponseEntity.ok(0); // Không có khuyến mãi, trả về 0
	        }
		
	    }
    
    
	  @GetMapping("/price-range/{productId}")
	    public ResponseEntity<ProductPriceRangeDTO> getProductPriceRange(@PathVariable("productId") Integer productId) {
	        List<VariantProduct> variants = vpr.findByProductId(productId);
	        
	        if (variants.isEmpty()) {
	            return ResponseEntity.notFound().build(); 
	        }

	        
	        double minPrice = variants.stream()
	            .min(Comparator.comparingDouble(VariantProduct::getPrice))
	            .map(VariantProduct::getPrice)
	            .orElse(0.0);

	        double maxPrice = variants.stream()
	            .max(Comparator.comparingDouble(VariantProduct::getPrice))
	            .map(VariantProduct::getPrice)
	            .orElse(0.0);

	        ProductPriceRangeDTO priceRangeDTO = new ProductPriceRangeDTO(minPrice, maxPrice);
	        return ResponseEntity.ok(priceRangeDTO);
	    }
	    @GetMapping("/search")
	    public ResponseEntity<List<Object[]>> searchProducts(@RequestParam("keyword") String keyword) {
	   //hàm tk
	        List<Object[]> products = productRepository.searchProductsWithLowestPrice(keyword);
	        return ResponseEntity.ok(products);
	    }

	    
	    
	    @GetMapping("/overall-price-range")
	    public Map<String, Object> getOverallPriceRange() {
	        PriceRangeDTO result = variantProductRepository.findOverallPriceRange();
	        Map<String, Object> response = new HashMap<>();
	        
	        if (result != null) {
	            response.put("minPrice", result.getMinPrice());
	            response.put("maxPrice", result.getMaxPrice());
	        } else {
	            response.put("minPrice", 0);
	            response.put("maxPrice", 0);
	        }
	        
	        return response;
	    }
}
