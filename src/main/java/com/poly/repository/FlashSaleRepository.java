package com.poly.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.model.ActivitySale;
import com.poly.model.FlashSale;

public interface FlashSaleRepository extends JpaRepository<FlashSale, Integer>{
	@Query("SELECT fs FROM FlashSale fs LEFT JOIN FETCH fs.activitySales WHERE fs.id = :id")
	Optional<FlashSale> findByIdWithActivitySales(@Param("id") Integer id);
	
	
	List<FlashSale> findByStatusTrue();
	
	@Query("SELECT fs FROM FlashSale fs " +
		       "LEFT JOIN FETCH fs.activitySales a " +
		       "WHERE fs.status = true " + 
		       "AND a.created_date <= CURRENT_TIMESTAMP " + 
		       "AND a.expiration_date > CURRENT_TIMESTAMP") 
		List<FlashSale> findValidFlashSales();
}
