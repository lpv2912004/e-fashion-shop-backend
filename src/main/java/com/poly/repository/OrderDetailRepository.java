package com.poly.repository;

import com.poly.dto.DailyOrderCountDTO;
import com.poly.dto.DailyRevenueDTO;
import com.poly.dto.FlashSaleStatisticsDTO;
import com.poly.dto.ProductInventoryDTO;
import com.poly.dto.ProductVariantDTO;
import com.poly.dto.ProductsDTo;
import com.poly.dto.UserStatisticsDTO;
import com.poly.dto.VoucherStatisticsDTO;
import com.poly.model.OrderDetail;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
	
	  List<OrderDetail> findByOrderId(Integer orderId);
	  
	  @Query("SELECT o FROM OrderDetail o WHERE o.order.id = ?1")
	  List<OrderDetail> findbyOrderId(Integer id);
	  
	  @Query("SELECT SUM(od.quantity) " +
	           "FROM OrderDetail od " +
	           "JOIN od.order o " +
	           "WHERE od.variantProd.product.id = :productId AND o.state.id = 9")
	    Integer countSuccessfulOrdersByProductId(@Param("productId") Integer productId);
	 
	
	  
//sản phẩm
//	  @Query("SELECT new com.poly.dto.ProductsDTo(p.name_prod, CONCAT(od.name_color, ' - ', od.name_size), od.price, SUM(od.quantity)) " +
//		       "FROM OrderDetail od " +
//		       "JOIN Product p ON od.variantProd.product.id = p.id " + // Giữ lại phần lấy sản phẩm từ Product
//		       "JOIN State st ON od.order.state.id = st.id " + 
//		       "WHERE st.id = 9 " + 
//		       "AND od.order.created_date BETWEEN :fromDate AND :toDate " +
//		       "GROUP BY p.name_prod, od.name_color, od.name_size, od.price")
//		List<ProductsDTo> findSoldProductsBetweenDates(@Param("fromDate") Timestamp fromDate, 
//		                                               @Param("toDate") Timestamp toDate);

	  @Query("SELECT new com.poly.dto.ProductsDTo(p.id, p.name_prod, CONCAT(od.name_color, ' - ', od.name_size), od.price, SUM(CAST(od.quantity AS long)), o.created_date) " +
		       "FROM OrderDetail od " +
		       "JOIN od.order o " +
		       "JOIN Product p ON od.variantProd.product.id = p.id " +
		       "JOIN State st ON o.state.id = st.id " +
		       "WHERE st.id = 9 " +
		       "AND o.created_date BETWEEN :fromDate AND :toDate " +
		       "GROUP BY p.id, p.name_prod, od.name_color, od.name_size, od.price, o.created_date")
		List<ProductsDTo> findSoldProductsBetweenDates(@Param("fromDate") LocalDateTime fromDateTime,
		                                               @Param("toDate") LocalDateTime toTimestamp);


	  
	  
	  
	  
	  
	
	  
	  
		// Truy vấn doanh thu chỉ tính các sản phẩm đã bán và có trạng thái đã giao
		@Query("SELECT SUM(od.quantity * od.price) FROM OrderDetail od " +
		       "JOIN od.order o " +
		       "WHERE o.created_date BETWEEN :fromDate AND :toDate " +
		       "AND o.state.id = 9 ")
		Double findRevenueBetweenDates(@Param("fromDate") LocalDateTime fromDateTime, @Param("toDate") LocalDateTime toDateTime);

		//doanh thu ròng
		@Query("SELECT SUM(o.total_cash) FROM Order o " +
			       "WHERE o.created_date BETWEEN :fromDate AND :toDate " +
			       "AND o.state.id = 9")
			Double findTotalCashBetweenDates(@Param("fromDate") LocalDateTime fromDateTime, @Param("toDate") LocalDateTime toDateTime);

		
		
		
		// Truy vấn tồn kho
		@Query("SELECT new com.poly.dto.ProductInventoryDTO(p.name_prod, vp.quantity, vp.price, c.color_name, s.name_size, p.image_prod, vp.image_variant) " +
			       "FROM VariantProduct vp " +
			       "JOIN Product p ON vp.product.id = p.id " +
			       "JOIN Color c ON vp.color.id = c.id " +
			       "JOIN Size s ON vp.size.id = s.id " +
			       "WHERE vp.quantity > 0")
			List<ProductInventoryDTO> findInventory();

		
		//'''''''''''''''''''''''''''''''''''''''''''''
		
//dt
		@Query("SELECT new com.poly.dto.UserStatisticsDTO(u.username, COUNT(o.id), SUM(od.quantity * od.price), " +
	               "new com.poly.dto.ProductDetailsDTO(p.name_prod, od.quantity, od.price, od.name_color, od.name_size)) " +
	               "FROM User u " +
	               "JOIN u.orders o " +
	               "JOIN OrderDetail od ON o.id = od.order.id " +
	               "JOIN VariantProduct vp ON od.variantProd.id = vp.id " +
	               "JOIN Product p ON vp.product.id = p.id " +
	               "WHERE o.created_date BETWEEN :fromDate AND :toDate " +
	               "AND o.state.id = 9 " + 
	               "GROUP BY u.username, p.name_prod, od.quantity, od.price, od.name_color, od.name_size")
	List<UserStatisticsDTO> findUserStatistics(@Param("fromDate") LocalDateTime fromTimestamp, @Param("toDate") LocalDateTime toTimestamp);

		
		
		
		
		
		
		
		//mvc
//		@Query("SELECT new com.poly.dto.VoucherStatisticsDTO(v.code, COUNT(ov.id), SUM(ov.discount_voucher)) " +
//			       "FROM Voucher v " +
//			       "JOIN OrderMoreVoucher ov ON v.id = ov.voucher.id " +
//			       "WHERE v.created_date BETWEEN :fromDate AND :toDate " +
//			       "OR v.expiration_date BETWEEN :fromDate AND :toDate " +
//			       "GROUP BY v.code")
//			List<VoucherStatisticsDTO> findVoucherStatistics(@Param("fromDate") Timestamp fromDate, @Param("toDate") Timestamp toDate);
//
//		
//		@Query("SELECT new com.poly.dto.VoucherStatisticsDTO(v.code, COUNT(ov.id), SUM(ov.discount_voucher), o.id, u.username) " +
//			       "FROM Voucher v " +
//			       "JOIN OrderMoreVoucher ov ON v.id = ov.voucher.id " +
//			       "JOIN ov.order o " +
//			       "JOIN o.user u " +
//			       "WHERE (v.created_date BETWEEN :fromDate AND :toDate " +
//			       "OR v.expiration_date BETWEEN :fromDate AND :toDate) " +
//			       "GROUP BY v.code, o.id, u.username")
//			List<VoucherStatisticsDTO> findVoucherStatistics(@Param("fromDate") Timestamp fromDate, 
//			                                                 @Param("toDate") Timestamp toDate);
//
//		
		@Query("SELECT new com.poly.dto.VoucherStatisticsDTO(v.code, COUNT(ov.id), SUM(ov.discount_voucher), o.id, u.username, t.nameTypeVoucher, v.condition) " +
			       "FROM Voucher v " +
			       "JOIN v.orderMoreVouchers ov " +
			       "JOIN ov.order o " +
			       "JOIN o.user u " +
			       "JOIN v.typeVoucher t " +
			       "WHERE (v.created_date BETWEEN :fromDate AND :toDate " +
			       "OR v.expiration_date BETWEEN :fromDate AND :toDate) " +
			       "GROUP BY v.code, o.id, u.username, t.nameTypeVoucher, v.condition")
			List<VoucherStatisticsDTO> findVoucherStatistics(@Param("fromDate") LocalDateTime fromTimestamp, 
			                                                 @Param("toDate") LocalDateTime toTimestamp);

		
		
		
		
		
//thống kê flsale
//		@Query("SELECT new com.poly.dto.FlashSaleStatisticsDTO(fs.name_FS, SUM(od.quantity), SUM(od.quantity * od.price)) " +
//			       "FROM FlashSale fs " +
//			       "JOIN ActivitySale as ON fs.id = as.flashSale.id " +
//			       "JOIN OrderDetail od ON as.variantProduct.id = od.variantProd.id " +
//			       "GROUP BY fs.name_FS")
//			List<FlashSaleStatisticsDTO> findFlashSaleStatistics();


//		@Query("SELECT new com.poly.dto.FlashSaleStatisticsDTO(fs.name_FS, COUNT(od.id), SUM(od.quantity)) " +
//			       "FROM FlashSale fs " +
//			       "JOIN ActivitySale as ON fs.id = as.flashSale.id " +
//			       "JOIN OrderDetail od ON as.variantProduct.id = od.variantProd.id " +
//			       "WHERE fs.created_date BETWEEN :fromDate AND :toDate " +
//			       "GROUP BY fs.name_FS")
//			List<FlashSaleStatisticsDTO> findFlashSaleStatistics(@Param("fromDate") Timestamp fromDate, @Param("toDate") Timestamp toDate);
//

		@Query("SELECT new com.poly.dto.FlashSaleStatisticsDTO(" +
		           "fs.name_FS, " +
		           "COUNT(od.id), " +
		           "SUM(od.quantity), " +
		           "as.variantProduct.id, " +
		           "fs.id, " +
		           "o.id, " +  // Lấy id đơn hàng
		           "o.user.username) " +  // Lấy id người dùng
		       "FROM FlashSale fs " +
		       "JOIN fs.activitySales as " +
		       "JOIN OrderDetail od ON as.variantProduct.id = od.variantProd.id " +
		       "JOIN od.order o " +
		       "WHERE fs.created_date BETWEEN :fromDate AND :toDate " +
		       "AND o.state.id = 9 " +
		       "GROUP BY fs.name_FS, as.variantProduct.id, fs.id, o.id, o.user.username")
		List<FlashSaleStatisticsDTO> findFlashSaleStatistics(@Param("fromDate") LocalDateTime fromTimestamp, 
		                                                     @Param("toDate") LocalDateTime toTimestamp);

		
		//doanh thu từng ngày

		@Query("SELECT new com.poly.dto.DailyRevenueDTO(CAST(o.created_date AS date), SUM(od.quantity * od.price)) " +
			       "FROM OrderDetail od " +
			       "JOIN od.order o " +
			       "WHERE o.created_date BETWEEN :fromDate AND :toDate " +
			       "AND o.state.id = 9 " +  // Chỉ lấy các đơn hàng đã giao
			       "GROUP BY CAST(o.created_date AS date) " +
			       "ORDER BY CAST(o.created_date AS date) ASC")
			List<DailyRevenueDTO> findDailyRevenueBetweenDates(@Param("fromDate") LocalDateTime fromDateTime, @Param("toDate") LocalDateTime toDateTime);

		@Query("SELECT new com.poly.dto.DailyOrderCountDTO(CAST(o.created_date AS date), COUNT(o.id)) " +
			       "FROM Order o " +
			       "WHERE o.created_date BETWEEN :fromDate AND :toDate " +
			       "AND o.state.id = 9 " + // Chỉ lấy các đơn hàng đã giao
			       "GROUP BY CAST(o.created_date AS date) " +
			       "ORDER BY CAST(o.created_date AS date) ASC")
			List<DailyOrderCountDTO> findDailyOrderCountBetweenDates(@Param("fromDate") LocalDateTime fromDateTime, @Param("toDate") LocalDateTime toDateTime);

		
		
		

		
}
