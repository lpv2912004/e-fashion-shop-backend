package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.Order;
import com.poly.model.OrderMoreVoucher;

@Repository
public interface OrderMoreVoucherRepository extends JpaRepository<OrderMoreVoucher, Integer>{
List<OrderMoreVoucher> findByOrder(Order order);
}
