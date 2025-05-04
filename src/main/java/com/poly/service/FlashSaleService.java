package com.poly.service;


import com.poly.model.FlashSale;
import com.poly.model.ActivitySale;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
@Service
public interface FlashSaleService {
    List<FlashSale> findAllFlashSales();
    Optional<FlashSale> findFlashSaleById(Integer id);
    FlashSale createFlashSale(FlashSale flashSale);
    FlashSale updateFlashSale(Integer id, FlashSale flashSaleDetails);

    List<ActivitySale> findActivitySalesByFlashSaleId(Integer flashSaleId);
    ActivitySale createActivitySale(ActivitySale activitySale, Integer flashSaleId);
 
	Optional<FlashSale> findFlashSaleById(int id);
	void deleteFlashSale(Integer id);


}
