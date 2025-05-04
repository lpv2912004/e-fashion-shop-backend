package com.poly.repository;

import com.poly.dto.OrderDetailWithOrderDTO;
import com.poly.model.Address;
import com.poly.model.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.poly.model.OrderDetail;
import com.poly.model.User;
import com.poly.model.VariantProduct;


public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	
	  @Query("SELECT SUM(o.total_cash) FROM Order o")
	    Double getTotalRevenue();
	  
	  @Query("SELECT SUM(o.total_cash) FROM Order o WHERE YEAR(o.created_date) = :year")
	    Double getRevenueByYear(@Param("year") int year);

	    @Query("SELECT SUM(o.total_cash) FROM Order o WHERE MONTH(o.created_date) = :month AND YEAR(o.created_date) = :year")
	    Double getRevenueByMonth(@Param("month") int month, @Param("year") int year);

	    @Query("SELECT SUM(o.total_cash) FROM Order o WHERE WEEK(o.created_date) = :week AND YEAR(o.created_date) = :year")
	    Double getRevenueByWeek(@Param("week") int week, @Param("year") int year);

	    @Query("SELECT SUM(o.total_cash) FROM Order o WHERE DAY(o.created_date) = :day AND MONTH(o.created_date) = :month AND YEAR(o.created_date) = :year")
	    Double getRevenueByDay(@Param("day") int day, @Param("month") int month, @Param("year") int year);
	    
	    @Query("SELECT o FROM Order o WHERE o.user.id = ?1 AND o.accept_order = TRUE")
	    List<Order> findALlOrderByUser(Integer userId);
	    
	    List<Order> findByAddress(Address address);
	    
	    @Query("SELECT o FROM Order o WHERE o.accept_order = TRUE")
	    List<Order> findAllAccept_order();
	    

	    @Query("SELECT new com.poly.dto.OrderDetailWithOrderDTO(od.id, od.quantity, od.price, od.full_address, od.name_color, od.name_size, od.discount_FS, od.name_FS, " +
	    	       "o.delivery_fee, o.total_cash, o.accept_order, o.user.id, o.state.id, o.methodPayment.id, od.variantProd.id, o.created_date) " +
	    	       "FROM OrderDetail od " +
	    	       "JOIN od.order o")
	    	List<OrderDetailWithOrderDTO> getOrderDetailsWithOrderInfo();


	    
	    @Query("SELECT o FROM Order o WHERE CAST(o.created_date AS date) = CAST(GETDATE() AS date) AND o.state.id = 9")
	    List<Order> findOrdersTodayWithStateDelivered();

	    @Query("SELECT SUM(o.total_cash) FROM Order o WHERE CAST(o.created_date AS date) = CAST(GETDATE() AS date) AND o.state.id = 9")
	    Double findTotalRevenueTodayWithStateDelivered();
	    
	    
	    @Query(value = """
	    	    SELECT DATENAME(weekday, o.created_date) AS day, SUM(o.total_cash) AS total
	    	    FROM Orders o
	    	    WHERE DATEPART(week, o.created_date) = DATEPART(week, GETDATE())
	    	    AND YEAR(o.created_date) = YEAR(GETDATE())
	    	    GROUP BY DATENAME(weekday, o.created_date), DATEPART(weekday, o.created_date)
	    	    ORDER BY DATEPART(weekday, o.created_date) ASC
	    	    """, nativeQuery = true)
	    	    List<Object[]> findWeeklySales();
	    	    
	    	    @Query("SELECT o FROM Order o " +
	 	    	       "JOIN FETCH o.orderDetails od " +
	 	    	       "WHERE o.user.id = :userId")
	 	    	List<Order> findOrdersByUserId(@Param("userId") Integer userId);
	  
}
