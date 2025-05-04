package com.poly.admin;

import com.poly.dto.FlashSaleDTO;
import com.poly.dto.ActivitySaleDTO;
import com.poly.dto.ConflictCheckDTO;
import com.poly.model.FlashSale;
import com.poly.model.User;
import com.poly.model.VariantProduct;
import com.poly.model.ActivitySale;
import com.poly.service.ActivitySaleService;
import com.poly.service.FlashSaleService;
import com.poly.service.ProductService;
import com.poly.service.UserService;
import com.poly.service.VariantProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/api/flashsales")
@CrossOrigin(origins = "*")
public class AdminFlashSaleRestController {

    @Autowired
    private FlashSaleService flashSaleService;
    @Autowired
    private ActivitySaleService activitySaleService;
    @Autowired
    private UserService userService;
    @Autowired
    private VariantProductService variantProductService;

    @GetMapping
    public ResponseEntity<List<FlashSaleDTO>> getAllFlashSales() {
        List<FlashSale> flashSales = flashSaleService.findAllFlashSales();
        List<FlashSaleDTO> flashSaleDTOs = flashSales.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(flashSaleDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlashSaleDTO> getFlashSaleById(@PathVariable Integer id) {
        Optional<FlashSale> flashSale = flashSaleService.findFlashSaleById(id);
        return flashSale.map(sale -> ResponseEntity.ok(convertToDTO(sale)))
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

 
    @PostMapping
    public ResponseEntity<FlashSaleDTO> createFlashSaleWithActivity(@RequestBody FlashSaleDTO flashSaleDTO) {
        FlashSale flashSale = new FlashSale();
        flashSale.setName_FS(flashSaleDTO.getName_FS());
        flashSale.setActivitySales(new ArrayList<>());

  
        flashSale.setStatus(flashSaleDTO.getStatus());

   
        Optional<User> user = userService.findById(flashSaleDTO.getId_user());
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        flashSale.setUser(user.get());

        FlashSale savedFlashSale = flashSaleService.createFlashSale(flashSale);

        List<ActivitySaleDTO> activitySaleDTOs = flashSaleDTO.getActivitySales();
        for (ActivitySaleDTO activitySaleDTO : activitySaleDTOs) {
            ActivitySale activitySale = new ActivitySale();
            activitySale.setDiscount_percent(activitySaleDTO.getDiscountPercent());
            activitySale.setCreated_date(activitySaleDTO.getCreatedDate()); 
            activitySale.setExpiration_date(activitySaleDTO.getExpirationDate());
            activitySale.setFlashSale(savedFlashSale);

            Optional<VariantProduct> variantProduct = variantProductService.findById(activitySaleDTO.getVariantProductId());
            if (!variantProduct.isPresent()) {
                return ResponseEntity.badRequest().body(null);
            }
            activitySale.setVariantProduct(variantProduct.get());

            activitySaleService.createActivitySale(activitySale);
        }

        FlashSaleDTO resultDTO = convertToDTO(savedFlashSale);
        return ResponseEntity.ok(resultDTO);
    }




    private FlashSaleDTO convertToDTO(FlashSale flashSale) {
        FlashSaleDTO dto = new FlashSaleDTO();
        dto.setId(flashSale.getId());
        dto.setName_FS(flashSale.getName_FS());
        dto.setCreated_date(flashSale.getCreated_date());
        dto.setId_user(flashSale.getUser().getId());
        dto.setStatus(flashSale.getStatus());

        // Kiểm tra null cho activitySales
        List<ActivitySaleDTO> activitySaleDTOs = Optional.ofNullable(flashSale.getActivitySales())
            .map(activitySales -> activitySales.stream()
                .map(this::convertToActivitySaleDTO)
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList()); 

        dto.setActivitySales(activitySaleDTOs);

        return dto;
    }


    private ActivitySaleDTO convertToActivitySaleDTO(ActivitySale activitySale) {
        ActivitySaleDTO dto = new ActivitySaleDTO();
        dto.setId(activitySale.getId());
        dto.setDiscountPercent(activitySale.getDiscount_percent());
        dto.setCreatedDate(activitySale.getCreated_date());
        dto.setExpirationDate(activitySale.getExpiration_date());
        
        if (activitySale.getVariantProduct() != null) {
            dto.setVariantProductId(activitySale.getVariantProduct().getId());
            dto.setVariantProductName(activitySale.getVariantProduct().getProduct().getName_prod());
            dto.setColor(activitySale.getVariantProduct().getColor().getColor_name());
            dto.setSize(activitySale.getVariantProduct().getSize().getName_size());
            
            // Gán URL ảnh từ biến thể
            dto.setImageUrl(activitySale.getVariantProduct().getImage_variant());
        } else {
            dto.setVariantProductId(null);
            dto.setImageUrl(null); // Trường hợp không có ảnh
        }

        return dto;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<FlashSaleDTO> updateFlashSaleStatus(@PathVariable Integer id, @RequestBody FlashSaleDTO flashSaleDTO) {
        // Tìm FlashSale theo ID
        Optional<FlashSale> optionalFlashSale = flashSaleService.findFlashSaleById(id);
        if (!optionalFlashSale.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Cập nhật trạng thái của FlashSale
        FlashSale flashSale = optionalFlashSale.get();
        flashSale.setStatus(flashSaleDTO.getStatus()); 

        // Lưu FlashSale sau khi cập nhật trạng thái
        flashSaleService.createFlashSale(flashSale);

        FlashSaleDTO resultDTO = convertToDTO(flashSale); // Chuyển đổi đối tượng sau khi cập nhật
        return ResponseEntity.ok(resultDTO); // Trả về kết quả thành công
    }

    @DeleteMapping("/activity/{id}")
    public ResponseEntity<Void> deleteActivitySale(@PathVariable Integer id) {
        Optional<ActivitySale> activitySaleOptional = activitySaleService.findById(id);
        if (!activitySaleOptional.isPresent()) {
            System.out.println("ActivitySale với ID: " + id + " không tồn tại.");
            return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy ActivitySale
        }

        // Lấy ActivitySale và FlashSale liên quan
        ActivitySale activitySale = activitySaleOptional.get();
        FlashSale flashSale = activitySale.getFlashSale(); // Lấy FlashSale từ ActivitySale

        try {
            // Xóa ActivitySale trước
            activitySaleService.deleteById(id);
            System.out.println("ActivitySale với ID: " + id + " đã bị xóa.");

            // Kiểm tra số lượng ActivitySale còn lại của FlashSale
            List<ActivitySale> remainingActivitySales = activitySaleService.findAllByFlashSaleId(flashSale.getId());

            if (remainingActivitySales.isEmpty()) {
       
                // Nếu không còn hoạt động nào, xóa luôn FlashSale
                flashSaleService.deleteFlashSale(flashSale.getId());
                System.out.println("FlashSale với ID: " + flashSale.getId() + " đã bị xóa vì không còn hoạt động nào.");
            }

            return ResponseEntity.noContent().build(); // Trả về 204 No Content nếu thành công
        } catch (Exception e) {
            // Ghi log lỗi nếu có vấn đề trong quá trình xóa
            System.err.println("Lỗi khi xóa ActivitySale hoặc FlashSale: " + e.getMessage());
            return ResponseEntity.status(500).build(); // Trả về 500 Internal Server Error nếu có lỗi
        }
    }


//    @PostMapping("/check-conflict")
//    public ResponseEntity<Map<String, Object>> checkConflict(@RequestBody ConflictCheckDTO conflictCheckDTO) {
//        boolean hasConflict = activitySaleService.hasConflict(
//            conflictCheckDTO.getVariantIds(),
//            conflictCheckDTO.getStartDate(),
//            conflictCheckDTO.getEndDate()
//        );
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("conflict", hasConflict);
//
//        if (hasConflict) {
//            response.put("message", "Biến thể này đã được giảm giá trong khoảng thời gian đã chọn.");
//        }
//
//        return ResponseEntity.ok(response);
//    }


//    @PostMapping("/check-conflict")
//    public ResponseEntity<Map<String, Object>> checkConflict(@RequestBody ConflictCheckDTO conflictCheckDTO) {
//        System.out.println("Received variantIds: " + conflictCheckDTO.getVariantIds());
//        System.out.println("Start date: " + conflictCheckDTO.getStartDate());
//        System.out.println("End date: " + conflictCheckDTO.getEndDate());
//
//        boolean hasConflict = activitySaleService.hasConflict(
//            conflictCheckDTO.getVariantIds(),
//            conflictCheckDTO.getStartDate(),
//            conflictCheckDTO.getEndDate()
//        );
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("conflict", hasConflict);
//
//        if (hasConflict) {
//            response.put("message", "Biến thể này đã được giảm giá trong khoảng thời gian đã chọn.");
//        }
//
//        return ResponseEntity.ok(response);
//    }
    @PostMapping("/check-conflict")
    public ResponseEntity<Map<String, Object>> checkConflict(@RequestBody ConflictCheckDTO conflictCheckDTO) {
        System.out.println("Received variantIds: " + conflictCheckDTO.getVariantIds());
        System.out.println("Start date: " + conflictCheckDTO.getStartDate());
        System.out.println("End date: " + conflictCheckDTO.getEndDate());

     
        List<ActivitySale> conflictingSales = activitySaleService.getConflictingSales(
            conflictCheckDTO.getVariantIds(),
            conflictCheckDTO.getStartDate(),
            conflictCheckDTO.getEndDate()
        );

        Map<String, Object> response = new HashMap<>();
        boolean hasConflict = !conflictingSales.isEmpty();
        response.put("conflict", hasConflict);

        if (hasConflict) {
            response.put("message", "Một số biến thể đã được giảm giá trong khoảng thời gian đã chọn.");
            
            List<Integer> conflictingVariantIds = conflictingSales.stream()
                .map(sale -> sale.getVariantProduct().getId())
                .distinct()
                .collect(Collectors.toList());

            response.put("conflictingVariantIds", conflictingVariantIds);
        }

        return ResponseEntity.ok(response);
    }


}

