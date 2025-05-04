package com.poly.service.implement;



import com.poly.model.ActivitySale;
import com.poly.repository.ActivityFlashSaleRepository;
import com.poly.service.ActivitySaleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ActivitySaleServiceImpl implements ActivitySaleService {

    @Autowired
    private ActivityFlashSaleRepository activitySaleRepository;

    @Override
    public ActivitySale createActivitySale(ActivitySale activitySale) {
        return activitySaleRepository.save(activitySale); // Lưu ActivitySale vào CSDL
    }

    @Override
    public Optional<ActivitySale> findById(Integer id) {
        return activitySaleRepository.findById(id); // Tìm ActivitySale theo ID
    }

    @Override
    public List<ActivitySale> findAll() {
        return activitySaleRepository.findAll(); // Lấy tất cả ActivitySales
    }

    @Override
    public void deleteById(Integer id) {
        activitySaleRepository.deleteById(id); // Xóa ActivitySale theo ID
    }
    
    @Override
    public List<ActivitySale> findAllByFlashSaleId(Integer flashSaleId) {
        return activitySaleRepository.findByFlashSaleId(flashSaleId); // Tìm tất cả ActivitySale liên quan đến FlashSale
    }

	@Override
	public boolean hasConflict(List<Integer> variantIds, Date startDate, Date endDate) {
        for (Integer variantId : variantIds) {
            List<ActivitySale> conflictingSales = activitySaleRepository.findConflictingSales(variantId, startDate, endDate);
            if (!conflictingSales.isEmpty()) {
                return true; // Có xung đột
            }
        }
        return false; // Không có xung đột
    }
	@Override
	public List<ActivitySale> getConflictingSales(List<Integer> variantIds, Date startDate, Date endDate) {
	    List<ActivitySale> conflictingSales = new ArrayList<>();
	    for (Integer variantId : variantIds) {
	        List<ActivitySale> conflicts = activitySaleRepository.findConflictingSales(variantId, startDate, endDate);
	        conflictingSales.addAll(conflicts);
	    }
	    return conflictingSales;
	}
    
}

