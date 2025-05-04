package com.poly.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.poly.dto.CheckoutElement;

@Service
public class CheckoutStorageService {
	 // Lưu CheckoutElement tạm thời với transactionId làm key
    private Map<String, CheckoutElement> checkoutMap = new HashMap<>();

    // Lưu CheckoutElement vào Map
    public void saveCheckoutElement(String transactionId, CheckoutElement checkoutElement) {
        checkoutMap.put(transactionId, checkoutElement);
    }

    // Lấy CheckoutElement ra khỏi Map
    public CheckoutElement getCheckoutElement(String transactionId) {
        return checkoutMap.get(transactionId);
    }

    // Xóa CheckoutElement sau khi xử lý
    public void removeCheckoutElement(String transactionId) {
        checkoutMap.remove(transactionId);
    }
}
