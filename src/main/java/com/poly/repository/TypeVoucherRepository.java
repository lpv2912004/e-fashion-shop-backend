package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.TypeVoucher;

@Repository
public interface TypeVoucherRepository extends JpaRepository<TypeVoucher, Integer>{

}
