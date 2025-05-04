package com.poly.repository;

import com.poly.dto.PriceRangeDTO;
import com.poly.dto.ProductSaleDTO;
import com.poly.model.OrderDetail;
import com.poly.model.VariantProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantProductRepository extends JpaRepository<VariantProduct, Integer> {
	Optional<VariantProduct> findByProduct_IdAndColor_IdAndSize_Id(Integer productId, Integer colorId, Integer sizeId);

	List<VariantProduct> findByProductId(Integer productId);

	List<VariantProduct> findByProduct_Id(Integer productId);

	long countByProductId(Integer productId);

	@Query("SELECT SUM(v.quantity) FROM VariantProduct v WHERE v.product.id = :productId")
	int sumQuantityByProductId(@Param("productId") Integer productId);

//    @Query("SELECT new com.poly.dto.ProductSaleDTO"
//    		+ "(v.product.id, "
//    		+ "v.product.name_prod, "
//    		+ "MIN(v.price), "
//    		+ "MIN(v.price - (v.price * act.discount_percent / 100)), MAX(act.discount_percent), MIN(v.image_variant)) " +
// 	       "FROM VariantProduct v " +
// 	       "JOIN v.flashSale fs " +
// 	       "JOIN fs.activitySales act " +
// 	       "WHERE fs.status = true OR  act.expiration_date > CURRENT_TIMESTAMP" +
// 	       "GROUP BY v.product.id, v.product.name_prod")
//    

//    @Query("SELECT new com.poly.dto.ProductSaleDTO"
//    		+ "(v.product.id, "
//    		+ "v.product.name_prod, "
//    		+ "MIN(v.price), "
//    		+ "MIN(v.price - (v.price * act.discount_percent / 100)), MAX(act.discount_percent), MIN(v.image_variant)) " +
//    	       "FROM VariantProduct v " +
//    	       "JOIN v.flashSale fs " +
//    	       "JOIN fs.activitySales act " +
//    	       "WHERE fs.status = true AND act.expiration_date > CURRENT_TIMESTAMP " +
//    	       "GROUP BY v.product.id, v.product.name_prod")
//    	List<ProductSaleDTO> findProductsOnSale();
	@Query("SELECT new com.poly.dto.ProductSaleDTO" + "(v.variantProduct.product.id, "
			+ "v.variantProduct.product.name_prod, " + "MIN(v.variantProduct.price), "
			+ "MIN(v.variantProduct.price - (v.variantProduct.price * v.discount_percent / 100)), "
			+ "MAX(v.discount_percent), " + "MIN(v.variantProduct.image_variant)) " + "FROM ActivitySale v "
			+ "JOIN v.flashSale fs " + "WHERE fs.status = true AND v.expiration_date > CURRENT_TIMESTAMP "
			+ "GROUP BY v.variantProduct.product.id, v.variantProduct.product.name_prod")
	List<ProductSaleDTO> findProductsOnSale();

// 	List<ProductSaleDTO> findProductsOnSale();

	@Query("SELECT o FROM VariantProduct o WHERE o.product.id = ?1 AND o.status_VP = 1")
	List<VariantProduct> takeAll(Integer id);
	
	
	 //vinh
    VariantProduct findByOrderDetails(OrderDetail orderDetail);
    
    
    @Query("SELECT new com.poly.dto.PriceRangeDTO(MIN(v.price), MAX(v.price)) FROM VariantProduct v")
	PriceRangeDTO findOverallPriceRange();



}
