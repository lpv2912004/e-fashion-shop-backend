package com.poly.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.model.ActivitySale;
import com.poly.model.FlashSale;
import com.poly.model.VariantProduct;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface ActivityFlashSaleRepository extends JpaRepository<ActivitySale, Integer>{
//    ActivitySale findByFlashSaleAndExpiryDate(FlashSale flashSale, Date expiryDate);
	
//	 List<ActivitySale> findByVariantProduct(VariantProduct variantProduct);
	@Query("SELECT a FROM ActivitySale a "
			+ "WHERE a.variantProduct.id = :variantId AND "
			+ "a.created_date <= CURRENT_TIMESTAMP AND a.expiration_date >= CURRENT_TIMESTAMP")
    Optional<ActivitySale> findActiveDiscountByVariantId(@Param("variantId") Integer variantId);
	
	 List<ActivitySale> findByFlashSaleId(Integer flashSaleId);

	 
	 @Query("SELECT a FROM ActivitySale a WHERE a.variantProduct.id = :variantId AND " +
	           "((a.created_date <= :endDate AND a.expiration_date >= :startDate) OR " +
	           "(a.created_date >= :startDate AND a.created_date <= :endDate))")
	    List<ActivitySale> findConflictingSales(@Param("variantId") Integer variantId,
	                                            @Param("startDate") Date startDate,
	                                            @Param("endDate") Date endDate);
}
