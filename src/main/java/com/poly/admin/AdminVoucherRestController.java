package com.poly.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poly.model.TypeVoucher;
import com.poly.model.Voucher;
import com.poly.service.TypeVoucherService;
import com.poly.service.VoucherService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin/api")
@CrossOrigin(origins = "*")
public class AdminVoucherRestController {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private TypeVoucherService typeVoucherService;  // Inject TypeVoucherService

    @GetMapping("/vouchers")
    public ResponseEntity<List<Voucher>> getAllVouchers() {
        List<Voucher> vouchers = voucherService.findAllVouchers();
        return ResponseEntity.ok(vouchers);
    }

    @GetMapping("/vouchers/{id}")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable Long id) {
        Optional<Voucher> voucher = voucherService.findById(id);
        return voucher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/vouchers")
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher voucher) {
        System.out.println("Received voucher: " + voucher);  
        Voucher createdVoucher = voucherService.saveVoucher(voucher);
        return ResponseEntity.ok(createdVoucher);
    }

    @PutMapping("/vouchers/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable Long id, @RequestBody Voucher voucherDetails) {
        Optional<Voucher> optionalVoucher = voucherService.findById(id);
        if (optionalVoucher.isPresent()) {
            Voucher voucher = optionalVoucher.get();
            voucher.setCode(voucherDetails.getCode());
            voucher.setDiscount(voucherDetails.getDiscount());
            voucher.setCreated_date(voucherDetails.getCreated_date());
            voucher.setExpiration_date(voucherDetails.getExpiration_date());
            voucher.setQuantity(voucherDetails.getQuantity());
            voucher.setStatus(voucherDetails.getStatus()); 
            voucher.setCondition(voucherDetails.getCondition());
            Voucher updatedVoucher = voucherService.saveVoucher(voucher);
            return ResponseEntity.ok(updatedVoucher);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/vouchers/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Long id) {
        boolean isRemoved = voucherService.deleteById(id);
        if (!isRemoved) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    // New endpoint to fetch TypeVoucher
    @GetMapping("/typeVouchers")
    public ResponseEntity<List<TypeVoucher>> getAllTypeVouchers() {
        List<TypeVoucher> typeVouchers = typeVoucherService.findAllTypeVouchers();
        return ResponseEntity.ok(typeVouchers);
    }
    
    
    @GetMapping("/vouchers/check-code/{code}")
    public ResponseEntity<?> checkVoucherCodeExists(@PathVariable String code) {
        boolean exists = voucherService.existsByCode(code);
        return ResponseEntity.ok().body(Map.of("exists", exists));
    }
}
