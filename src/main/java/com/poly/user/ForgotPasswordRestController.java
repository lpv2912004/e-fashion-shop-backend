package com.poly.user;

import com.poly.dto.ResetPasswordRequest;
import com.poly.model.Role;
import com.poly.model.Slide;
import com.poly.model.User;
import com.poly.repository.LikeRepository;
import com.poly.repository.RoleRepository;
import com.poly.repository.UserRepository;
import com.poly.service.EmailService;
import com.poly.service.OtpService;
import com.poly.service.SlideService;
import com.poly.service.TemporaryTokenStore;
import com.poly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class ForgotPasswordRestController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private OtpService otpService;

	@Autowired
	private EmailService mailService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	private TemporaryTokenStore tokenStore;
	
	@Autowired
	private LikeRepository likeRepository;

	@Autowired
    private SlideService slideService;
    
	//api slide
	@GetMapping("/all-slide")
	public ResponseEntity<List<Slide>> getAllSlides() {
		try {
			List<Slide> slides = slideService.getAllSlides();
			return new ResponseEntity<>(slides, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody Map<String, Object> emailRQ) {
		String email = (String) emailRQ.get("emailRQ");
		// Kiểm tra xem email có tồn tại không
		User user = userRepository.findByEmail(email);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email không tồn tại");
		}

		// Tạo token và lưu trữ trong bộ nhớ tạm thời
		String resetToken = tokenStore.createToken(email);

		// Gửi email chứa liên kết đặt lại mật khẩu
		String resetPasswordLink = "http://localhost:3000/resetpass?token=" + resetToken;
		mailService.sendResetPasswordEmail(user.getEmail(), resetPasswordLink);

		return ResponseEntity.ok("Link đặt lại mật khẩu đã được gửi đến email của bạn");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
		String token = resetPasswordRequest.getToken();
		String newPassword = resetPasswordRequest.getNewPassword();
		String confirmPassword = resetPasswordRequest.getConfirmPassword();

		// Kiểm tra mật khẩu và mật khẩu xác nhận có khớp không
		if (!newPassword.equals(confirmPassword)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mật khẩu và mật khẩu xác nhận không khớp");
		}

		// Lấy thông tin token và kiểm tra thời hạn
		TemporaryTokenStore.TokenInfo tokenInfo = tokenStore.getTokenInfo(token); // Truy cập vào lớp con TokenInfo
		if (tokenInfo == null || tokenInfo.getExpiryDate().isBefore(LocalDateTime.now())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token không hợp lệ hoặc đã hết hạn");
		}

		// Đặt lại mật khẩu cho người dùng
		String email = tokenInfo.getEmail();
		User user = userRepository.findByEmail(email);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại");
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);

		// Xóa token sau khi sử dụng
		tokenStore.removeToken(token);

		return ResponseEntity.ok("Mật khẩu đã được đặt lại thành công");
	}

	// Đăng ký qua email và gửi OTP
	@PostMapping("/register")
	public ResponseEntity<?> registerWithEmail(@RequestBody Map<String, Object> emailRQ) {
		String email = (String) emailRQ.get("email");
		// Kiểm tra email đã tồn tại trong cơ sở dữ liệu chưa
		User existingUser = userRepository.findByEmail(email);
		if (existingUser != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email đã được đăng ký.");
		}

		// Tạo và gửi OTP qua email (không cần kiểm tra email tồn tại trong DB)
		otpService.invalidateOtp(email);
		String otp = otpService.generateOtp(email);
		mailService.sendOtpregis(email, otp);

		return ResponseEntity.ok("OTP đã được gửi tới email của bạn.");
	}

	// Xác nhận OTP cho đăng ký
	@PostMapping("/verify-otp-register")
	public ResponseEntity<String> verifyOtpForRegister(@RequestBody Map<String, Object> request) {
		String email = (String) request.get("email");
		String otp = (String) request.get("enteredOtp");
		// Không kiểm tra sự tồn tại của email trong cơ sở dữ liệu
		boolean isOtpValid = otpService.isOtpValid(email, otp); // Xác thực OTP
		if (isOtpValid) {
			otpService.invalidateOtp(email); // Xóa OTP sau khi xác minh thành công
			return ResponseEntity.ok("OTP hợp lệ. Bạn có thể hoàn tất đăng ký.");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP không hợp lệ hoặc đã hết hạn.");
		}
	}

	@PostMapping("/create-account")
	public ResponseEntity<String> createAccount(@RequestBody Map<String, Object> request) throws ParseException {
		System.out.println("Received request: " + request);
		String email = (String) request.get("email");
		String username = (String) request.get("username");
		String password = (String) request.get("password");
		String confirmPassword = (String) request.get("confirmPassword");
		String fullName = (String) request.get("fullName");
		String phone = (String) request.get("phone");
		String birthday = (String) request.get("birthday");
		String gender = (String) request.get("gender");
		// Kiểm tra email có chưa
		User existingUser = userRepository.findByEmail(email);
		User existingName = userRepository.findByUsername(username);
		if (existingUser != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email đã được đăng ký.");
		}
		if (!password.equals(confirmPassword)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mật khẩu xác nhận không khớp.");
		}
		if (username == null || username.isEmpty() || password == null || password.isEmpty() || fullName == null
				|| fullName.isEmpty() || phone == null || phone.isEmpty() || birthday == null || birthday.isEmpty()
//                gender == null || gender.isEmpty()
		) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Các trường không được để trống.");
		}
		if (existingName != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usernam đã tồn tại!!");
		}
		// Xác minh và chuyển đổi giá trị gender
		Boolean genderValue = null;
		if (gender != null) {
			if ("male".equalsIgnoreCase(gender)) {
				genderValue = true; // Male sẽ là true (1)
			} else if ("female".equalsIgnoreCase(gender)) {
				genderValue = false; // Female sẽ là false (0)
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Giới tính không hợp lệ.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Giới tính không được để trống.");
		}

		// Mã hóa mật khẩu
		String encodedPassword = passwordEncoder.encode(password);
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dt1.parse(birthday);
		User newUser = new User();
		Role role = roleRepository.findById(2).get();
		newUser.setUsername(username);
		newUser.setPassword(encodedPassword);
		newUser.setFullName(fullName);
		newUser.setPhone(phone);
		newUser.setEmail(email);
		newUser.setGender(genderValue);
		newUser.setBirthday(date);
		newUser.setStatus_user(true); // Trạng thái user mặc định là true
		newUser.setRole(role);
		userService.save(newUser);

		return ResponseEntity.ok("Tài khoản đã được tạo thành công.");
	}
	
	// API để lấy số lượt thích của một sản phẩm
		@GetMapping("/count/{productId}")
		public long getLikesCountForProduct(@PathVariable Integer productId) {
			return likeRepository.countByProduct_IdAndIsLikedTrue(productId);
		}
}
