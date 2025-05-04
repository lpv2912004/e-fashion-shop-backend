package com.poly.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.FlashSaleDTO3;
import com.poly.dto.VariantDiscountDTO;
import com.poly.model.FlashSale;
import com.poly.repository.FlashSaleRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("user/api/flash-sales")
public class UserFlashSaleRestController {

    @Autowired
    private FlashSaleRepository flashSaleRepository;

    // Lấy danh sách các chương trình Flash Sale đang hoạt động cùng với thông tin giảm giá
    @GetMapping("/with-discounts")
    public ResponseEntity<List<FlashSaleDTO3>> getFlashSalesWithDiscounts() {
        List<FlashSale> flashSales = flashSaleRepository.findValidFlashSales();
        List<FlashSaleDTO3> flashSaleDTOs = flashSales.stream()
            .map(this::toFlashSaleDTO3)
            .collect(Collectors.toList());

        return ResponseEntity.ok(flashSaleDTOs);
    }

    // Phương thức chuyển đổi FlashSale thành FlashSaleDTO
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
}}

