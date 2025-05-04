package com.poly.admin;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.DailyOrderCountDTO;
import com.poly.dto.DailyRevenueDTO;
import com.poly.dto.FlashSaleStatisticsDTO;
import com.poly.dto.OrderStatisticsDTO;
import com.poly.dto.ProductInventoryDTO;
import com.poly.dto.ProductVariantDTO;
import com.poly.dto.ProductsDTo;
import com.poly.dto.UserStatisticsDTO;
import com.poly.dto.VoucherStatisticsDTO;
import com.poly.model.Order;
import com.poly.repository.OrderRepository;
import com.poly.service.OrderDetailService;
import com.poly.service.ProductService;
import com.poly.service.StatisticsService;

@RestController
@RequestMapping("/admin/api/statistics")
public class AdminStatisticRestController {
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private ProductService productService;
	@Autowired
	private OrderRepository orderRepository;

	// spbd
	@GetMapping("/sales-products")
	public ResponseEntity<?> getSalesProducts(@RequestParam String fromDate, @RequestParam String toDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate from = LocalDate.parse(fromDate, formatter);
			LocalDate to = LocalDate.parse(toDate, formatter);

			LocalDateTime fromDateTime = from.atStartOfDay();
			LocalDateTime toDateTime = to.isEqual(from) ? to.atTime(LocalTime.MAX) : to.atStartOfDay();

			List<ProductsDTo> productsSold = statisticsService.getSoldProductsBetweenDates(fromDateTime, toDateTime);
			return ResponseEntity.ok(productsSold);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid date format. Please use yyyy-MM-dd.");
		}
	}

//
	@GetMapping("/revenue")
	public ResponseEntity<?> getRevenue(@RequestParam String fromDate, @RequestParam String toDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate from = LocalDate.parse(fromDate, formatter);
			LocalDate to = LocalDate.parse(toDate, formatter);

			LocalDateTime fromDateTime = from.atStartOfDay(); // 00:00:00
			LocalDateTime toDateTime = to.isEqual(from) ? to.atTime(LocalTime.MAX) : to.atStartOfDay(); // 23:59:59 nếu
																										// cùng ngày,
																										// 00:00:00 nếu
																										// khác ngày

			double revenue = statisticsService.getRevenueBetweenDates(fromDateTime, toDateTime);
			return ResponseEntity.ok(revenue);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid date format. Please use yyyy-MM-dd.");
		}
	}
	
	
	@GetMapping("/revenue2")
	public ResponseEntity<?> getRevenue2(@RequestParam String fromDate, @RequestParam String toDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate from = LocalDate.parse(fromDate, formatter);
			LocalDate to = LocalDate.parse(toDate, formatter);

			LocalDateTime fromDateTime = from.atStartOfDay(); // 00:00:00
			LocalDateTime toDateTime = to.isEqual(from) ? to.atTime(LocalTime.MAX) : to.atStartOfDay(); // 23:59:59 nếu
																										// cùng ngày,
																										// 00:00:00 nếu
																										// khác ngày

			double revenue = statisticsService.getRevenueBetweenDates2(fromDateTime, toDateTime);
			return ResponseEntity.ok(revenue);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid date format. Please use yyyy-MM-dd.");
		}
	}

	@GetMapping("/inventory")
	public ResponseEntity<?> getInventory() {
		List<ProductInventoryDTO> inventory = statisticsService.getInventory();
		return ResponseEntity.ok(inventory);
	}

	// nd
	@GetMapping("/user-statistics")
	public ResponseEntity<?> getUserStatistics(@RequestParam String fromDate, @RequestParam String toDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate from = LocalDate.parse(fromDate, formatter);
			LocalDate to = LocalDate.parse(toDate, formatter);

			LocalDateTime fromDateTime = from.atStartOfDay(); // 00:00:00
			LocalDateTime toDateTime = to.isEqual(from) ? to.atTime(LocalTime.MAX) : to.atStartOfDay(); // 23:59:59 nếu
																										// cùng ngày,
																										// 00:00:00 nếu
																										// khác ngày

			List<UserStatisticsDTO> userStatistics = orderDetailService.getUserStatistics(fromDateTime, toDateTime);
			return ResponseEntity.ok(userStatistics);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest()
					.body("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng yyyy-MM-dd.");
		}
	}

//vc
	@GetMapping("/voucher-statistics")
	public ResponseEntity<?> getVoucherStatistics(@RequestParam String fromDate, @RequestParam String toDate) {

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate from = LocalDate.parse(fromDate, formatter);
			LocalDate to = LocalDate.parse(toDate, formatter);

			LocalDateTime fromDateTime = from.atStartOfDay();
			LocalDateTime toDateTime = to.isEqual(from) ? to.atTime(LocalTime.MAX) : to.atStartOfDay();

			List<VoucherStatisticsDTO> voucherStatistics = statisticsService.getVoucherStatistics(fromDateTime,
					toDateTime);
			return ResponseEntity.ok(voucherStatistics);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid date format. Please use yyyy-MM-dd.");
		}
	}

	@GetMapping("/flash-sale-statistics")
	public ResponseEntity<?> getFlashSaleStatistics(@RequestParam String fromDate, @RequestParam String toDate) {

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate from = LocalDate.parse(fromDate, formatter);
			LocalDate to = LocalDate.parse(toDate, formatter);

			LocalDateTime fromDateTime = from.atStartOfDay();
			LocalDateTime toDateTime = to.isEqual(from) ? to.atTime(LocalTime.MAX) : to.atStartOfDay();

			List<FlashSaleStatisticsDTO> flashSaleStatistics = statisticsService.getFlashSaleStatistics(fromDateTime,
					toDateTime);
			return ResponseEntity.ok(flashSaleStatistics);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid date format. Please use yyyy-MM-dd.");
		}
	}

	@GetMapping("/daily-revenue")
	public ResponseEntity<List<DailyRevenueDTO>> getDailyRevenue(@RequestParam String fromDate,
			@RequestParam String toDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate from = LocalDate.parse(fromDate, formatter);
			LocalDate to = LocalDate.parse(toDate, formatter);

			LocalDateTime fromDateTime = from.atStartOfDay();
			LocalDateTime toDateTime = to.isEqual(from) ? to.atTime(LocalTime.MAX) : to.atStartOfDay();

			List<DailyRevenueDTO> revenueData = statisticsService.getDailyRevenue(fromDateTime, toDateTime);
			return ResponseEntity.ok(revenueData);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	@GetMapping("/daily-order-count")
	public ResponseEntity<List<DailyOrderCountDTO>> getDailyOrderCount(@RequestParam String fromDate,
			@RequestParam String toDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate from = LocalDate.parse(fromDate, formatter);
			LocalDate to = LocalDate.parse(toDate, formatter);

			LocalDateTime fromDateTime = from.atStartOfDay();
			LocalDateTime toDateTime = to.isEqual(from) ? to.atTime(LocalTime.MAX) : to.atStartOfDay();

			List<DailyOrderCountDTO> orderCountData = statisticsService.getDailyOrderCount(fromDateTime, toDateTime);
			return ResponseEntity.ok(orderCountData);
		} catch (DateTimeParseException e) {
			return null;
		}
	}


	@GetMapping("/product-variants")
	public ResponseEntity<List<ProductVariantDTO>> getProductVariants() {
		List<ProductVariantDTO> productVariants = productService.getAllProductVariants();
		return ResponseEntity.ok(productVariants);
	}

}
