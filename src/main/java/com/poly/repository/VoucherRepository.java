package com.poly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.dto.VoucherDTO;
import com.poly.dto.VoucherDTO2;
import com.poly.model.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
	Optional<Voucher>  findByCode(String code);

	@Query("SELECT v FROM Voucher v WHERE"
		    + " v.expiration_date >= CURRENT_TIMESTAMP AND v.created_date <= CURRENT_TIMESTAMP "
		    + "AND v.quantity > 0 "
		    + "AND v.status = TRUE "
		    + "AND v NOT IN (SELECT omv.voucher FROM OrderMoreVoucher omv WHERE omv.order.user.id = :userId)")
		List<Voucher> findAvailableVouchersForUser(@Param("userId") Long userId);
//	List<Voucher> findAvailableVouchersForUser(@Param("userId") Long userId);
	List<Voucher> findByStatusTrue();
	
	

//	List<Voucher> findAvailableVouchersForUser(@Param("userId") Long userId);
	
	@Query("SELECT v FROM Voucher v JOIN FETCH v.typeVoucher WHERE v.expiration_date > CURRENT_DATE AND v.status = true AND v.quantity >= 1")
	List<Voucher> findAvailableVouchersWithType();

	@Query("SELECT new com.poly.dto.VoucherDTO2(v.id, v.code, v.discount, v.expiration_date, v.quantity, v.typeVoucher.nameTypeVoucher) " +
		       "FROM Voucher v JOIN v.typeVoucher t WHERE v.expiration_date > CURRENT_DATE AND v.status = true AND v.quantity >= 1")
		List<VoucherDTO2> findAvailableVouchersWithType2();

	
    boolean existsByCode(String code);
}




