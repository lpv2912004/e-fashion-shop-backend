package com.poly.service.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.model.ActivitySale;
import com.poly.model.FlashSale;
import com.poly.repository.FlashSaleRepository;
import com.poly.service.FlashSaleService;
import com.poly.repository.ActivityFlashSaleRepository;

@Service
public class FlashSaleServiceImpl implements FlashSaleService {

    @Autowired
    private FlashSaleRepository flashSaleRepository;

    @Autowired
    private ActivityFlashSaleRepository activitySaleRepository;

    @Override
    public List<FlashSale> findAllFlashSales() {
        // Fetch all flash sales from the database
        return flashSaleRepository.findAll();
    }

    @Override
    public Optional<FlashSale> findFlashSaleById(Integer id) {
        // Fetch a flash sale by its ID
        return flashSaleRepository.findById(id);
    }

    @Override
    public FlashSale createFlashSale(FlashSale flashSale) {
        // Save a new flash sale to the database
        return flashSaleRepository.save(flashSale);
    }

    @Override
    public FlashSale updateFlashSale(Integer id, FlashSale flashSaleDetails) {
        // Update an existing flash sale
        Optional<FlashSale> flashSaleOptional = flashSaleRepository.findById(id);

        if (flashSaleOptional.isPresent()) {
            FlashSale flashSale = flashSaleOptional.get();
            flashSale.setName_FS(flashSaleDetails.getName_FS());
            flashSale.setCreated_date(flashSaleDetails.getCreated_date());
            flashSale.setUser(flashSaleDetails.getUser());
            return flashSaleRepository.save(flashSale);  // Save updated flash sale
        }
        return null;
    }

 

    @Override
    public List<ActivitySale> findActivitySalesByFlashSaleId(Integer flashSaleId) {
        // Fetch all activity sales by the FlashSale ID
        Optional<FlashSale> flashSale = flashSaleRepository.findById(flashSaleId);
        if (flashSale.isPresent()) {
            return flashSale.get().getActivitySales();
        }
        return null;
    }

    @Override
    public ActivitySale createActivitySale(ActivitySale activitySale, Integer flashSaleId) {
        // Create a new activity sale and associate it with a flash sale
        Optional<FlashSale> flashSale = flashSaleRepository.findById(flashSaleId);
        if (flashSale.isPresent()) {
            activitySale.setFlashSale(flashSale.get());
            return activitySaleRepository.save(activitySale);  // Save the new activity sale
        }
        return null;
    }



	@Override
	public Optional<FlashSale> findFlashSaleById(int id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
    public void deleteFlashSale(Integer id) {
	  flashSaleRepository.deleteById(id); // Xóa ActivitySale theo ID
    }

	
}

