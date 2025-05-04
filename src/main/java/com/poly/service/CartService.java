//package com.poly.service;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.poly.dto.CartDetailDTO;
//import com.poly.model.ActivitySale;
//import com.poly.model.CartDetail;
//import com.poly.model.FlashSale;
//import com.poly.model.VariantProduct;
//import com.poly.repository.ActivityFlashSaleRepository;
//import com.poly.repository.CartRepository;
//
//@Service
//public class CartService {
//	@Autowired
//    private CartRepository cartDetailRepository;
//
//    @Autowired
//    private ActivityFlashSaleRepository activitySaleRepository;
//
//    public List<CartDetailDTO> getCartDetailsWithDiscount(Integer userId) {
//        List<CartDetail> cartDetails = cartDetailRepository.findAllCartDetailByIdUser(userId);
//        List<CartDetailDTO> cartDetailResponseDTOs = new ArrayList<>();
//
//        for (CartDetail cartDetail : cartDetails) {
//            VariantProduct variantProduct = cartDetail.getVariantProd();
//            Double originalPrice = variantProduct.getPrice(); // Giá gốc của biến thể
//
//            // Lấy thông tin giảm giá nếu có
//            FlashSale flashSale = variantProduct.getFlashSale();
//            Double discountedPrice = originalPrice; // Giá sau khi giảm
//
//            if (flashSale != null) {
//                ActivitySale activitySale = activitySaleRepository
//                        .findByFlashSaleAndExpiryDate(flashSale, new Date());
//
//                if (activitySale != null) {
//                    // Tính toán giá sau khi giảm
//                    Integer discountPercent = activitySale.getDiscount_percent();
//                    discountedPrice = originalPrice * (1 - discountPercent / 100.0);
//                }
//            }
//
//            // Đóng gói dữ liệu
//            CartDetailDTO dto = new CartDetailDTO();
//            dto.setId(cartDetail.getId());
//            dto.setVariantProduct(variantProduct);
//            dto.setQuantity(cartDetail.getQuantity());
//            dto.setOriginalPrice(originalPrice);
//            dto.setDiscountedPrice(discountedPrice);
//
//            cartDetailResponseDTOs.add(dto);
//        }
//
//        return cartDetailResponseDTOs;
//    }
//}
