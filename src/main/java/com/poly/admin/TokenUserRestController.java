package com.poly.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.model.Order;
import com.poly.repository.OrderRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/api/sendtoken")
public class TokenUserRestController {
	@Autowired
	OrderRepository orderRepository;
	@PostMapping("device")
	public ResponseEntity<?> postMethodName(@RequestBody Map<String, Object> requestData) {
		Integer idOrder = (Integer) requestData.get("order");
		Order order = orderRepository.findById(idOrder).get();
		String token = order.getUser().getToken_device();	
		return ResponseEntity.ok(token);
	}
	
}
