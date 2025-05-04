package com.poly.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.model.User;
import com.poly.repository.UserRepository;

@RestController
@RequestMapping("/token")
@CrossOrigin(origins = "*")
public class TakeTokenDeviceOfAdmin {
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/device")
	public ResponseEntity<?> getMethodName() {
		User user = userRepository.findById(1).get();
		return ResponseEntity.ok(user.getToken_device());
	}

}
