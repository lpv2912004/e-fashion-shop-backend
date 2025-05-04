package com.poly.service;

import com.poly.model.ActivitySale;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface ActivitySaleService {
    ActivitySale createActivitySale(ActivitySale activitySale); // Tạo mới ActivitySale

    Optional<ActivitySale> findById(Integer id); // Tìm ActivitySale theo ID
    List<ActivitySale> findAll(); // Lấy tất cả ActivitySales
    void deleteById(Integer id); // Xóa ActivitySale theo ID
    List<ActivitySale> findAllByFlashSaleId(Integer integer);

	boolean hasConflict(List<Integer> variantIds, Date startDate, Date endDate);

	List<ActivitySale> getConflictingSales(List<Integer> variantIds, Date startDate, Date endDate);

    
}
