package com.poly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.model.Address;
import com.poly.model.User;
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
	
	@Query("SELECT o FROM Address o WHERE o.user.id = ?1 AND o.is_deleted = FALSE")
	List<Address> findListByUserId(Integer idUser);
	
	@Query("SELECT o FROM Address o WHERE o.user.id = ?1 AND o.is_default = TRUE")
	Address findByDefault(Integer id);

    List<Address> findByUserId(Integer userId);

    boolean existsByUser(User user);

}
