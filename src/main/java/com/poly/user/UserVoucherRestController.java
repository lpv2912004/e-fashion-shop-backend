package com.poly.user;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.VoucherDTO;
import com.poly.model.Order;
import com.poly.model.OrderMoreVoucher;
import com.poly.model.User;
import com.poly.model.Voucher;
import com.poly.repository.UserRepository;
import com.poly.repository.VoucherRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("user/api/voucher")
public class UserVoucherRestController {

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	UserRepository userRepository;

	@PostMapping("list")
	public ResponseEntity<?> getVouchers(@RequestBody Map<String, Object> reqMap) {

		Integer id_user = (Integer) reqMap.get("id_user");
		System.out.println(id_user);
		try {
			List<Voucher> list = voucherRepository.findAvailableVouchersForUser(Long.valueOf(id_user.longValue()));
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Lỗi load voucher");
		}

	}

	@GetMapping("/available")
	public ResponseEntity<List<VoucherDTO>> getAvailableVouchers() {
		// Retrieve available vouchers
		List<Voucher> vouchers = voucherRepository.findAvailableVouchersWithType();

		// Map to VoucherDTO including the TypeVoucher name
		List<VoucherDTO> voucherDTOs = vouchers.stream()
				.map(voucher -> new VoucherDTO(
						voucher.getId(), 
						voucher.getCode(), 
						voucher.getDiscount(),
						voucher.getExpiration_date(), 
						voucher.getDescription(), 
						voucher.getCondition(),
						voucher.getTypeVoucher() != null ? voucher.getTypeVoucher().getNameTypeVoucher() : null,
						voucher.getMax_voucher_apply()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(voucherDTOs);
	}

	@PostMapping("/searchCode")
	public ResponseEntity<?> getVoucherByCode(@RequestBody Map<String, Object> reqMap) {
		try {
			String code = (String) reqMap.get("code");
			Integer idUser = (Integer) reqMap.get("idUser");

			// Fetch user and voucher
			User user = userRepository.findById(idUser).orElse(null);
			if (user == null) {
				return ResponseEntity.status(404).body("User not found");
			}

			Voucher voucher = voucherRepository.findByCode(code).orElse(null);
			if (voucher == null) {
				return ResponseEntity.status(404).body("Voucher không tồn tại");
			}

			Date today = new Date();
			if (voucher.getExpiration_date().before(today)) {
				return ResponseEntity.status(404).body("Voucher đã hết hạn");
			}

			// Check if the user has used the voucher
			if (!user.getOrders().isEmpty()) {
				for (Order order : user.getOrders()) {
					if (!order.getOrderMoreVouchers().isEmpty()) {
						for (OrderMoreVoucher orderMoreVoucher : order.getOrderMoreVouchers()) {
							if (orderMoreVoucher.getVoucher().getCode().equals(code)) {
								return ResponseEntity.status(404).body("Bạn đã dùng Voucher này rồi!");
							}
						}
					}
				}
			}

			return ResponseEntity.ok(voucher);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Có lỗi xảy ra");
		}
	}

}
