//package com.poly.user;
//
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.poly.dto.UserDTO;
//import com.poly.model.User;
//import com.poly.repository.UserRepository;
//
//@CrossOrigin(origins = "*")
//@RestController
//@RequestMapping("/api/v1")
//public class AdminUserRestController {
//	@Autowired
//	UserRepository upsr;
//
//	@GetMapping("/list")
//	public ResponseEntity<List<User>> getAll() {
//		return ResponseEntity.ok(upsr.findAll());
//	}
//
////	@GetMapping("{username}/{password}")
////	public ResponseEntity<UserDTO> findUserByUsernamePassword(@PathVariable("username") String username,
////			@PathVariable("password") String password) {
////
////		User user = upsr.findByUsernameAndPassword(username, password);
////		if (user != null) {
////			UserDTO userDTO = new UserDTO(user.getId(), user.getFullName(), user.getImage_user());
////			return ResponseEntity.ok(userDTO);
////		} else {
////			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
////		}
////	}
//
//	@PostMapping("/post")
//	public ResponseEntity<User> postUser(@RequestBody User user) {
//		return ResponseEntity.ok(upsr.save(user));
//	}
//	
//	@PostMapping("/login")
//	public ResponseEntity<?> login(@RequestBody Map<String, Object> data) {
//	    String username = (String) data.get("username");
//	    String password = (String) data.get("password");
//	    User user = upsr.findByUsernameAndPassword(username, password);
//
//	    if (user == null) {
//	        // Trả về thông báo lỗi nếu không tìm thấy người dùng
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tên tài khoản hoặc mật khẩu");
//	    }
//
//	    // Trả về thông tin người dùng nếu đăng nhập thành công
//	    return ResponseEntity.ok(user);
//	}
//}
