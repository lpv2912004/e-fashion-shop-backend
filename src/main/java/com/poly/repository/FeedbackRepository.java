package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.model.Feedback;
import java.util.List;
import java.util.Optional;

import com.poly.model.OrderDetail;


public interface FeedbackRepository extends JpaRepository<Feedback, Long>{
	Optional<Feedback> findByOrderDetail(OrderDetail orderDetail);
	
    List<Feedback> findByOrderDetailId(Integer orderDetailId);
    
    
    
    @Query("SELECT f FROM Feedback f " +
            "JOIN f.orderDetail od " +
            "JOIN od.variantProd vp " +
            "JOIN vp.product p " +
            "WHERE p.id = :productId")
     List<Feedback> findByProductId(@Param("productId") Integer productId);
}
