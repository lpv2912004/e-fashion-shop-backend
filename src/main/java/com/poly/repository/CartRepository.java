package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.model.CartDetail;
import com.poly.model.User;

public interface CartRepository extends JpaRepository<CartDetail, Integer>{
	@Query("SELECT o FROM CartDetail o WHERE o.user.id = ?1")
	public List<CartDetail> findAllCartDetailByIdUser(Integer id_user);

	@Query("SELECT c FROM CartDetail c WHERE c.user.id = :userId AND c.variantProd.id = :variantId")
	CartDetail findByUserIdAndVariantProdId(@Param("userId") Integer userId, @Param("variantId") Integer variantId);

	List<CartDetail> findByUserId(Integer userId);

	void deleteByUserId(Integer userId);

	@Modifying
	@Query("DELETE FROM CartDetail c WHERE c.user.id = :userId AND c.variantProd.id = :variantProdId")
	void deleteByUserIdAndVariantProdId(@Param("userId") Integer userId, @Param("variantProdId") Integer variantProdId);
}
