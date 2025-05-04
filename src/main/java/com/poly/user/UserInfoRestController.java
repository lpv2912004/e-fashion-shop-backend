package com.poly.user;

import com.poly.dto.ImageUploadRequest;
import com.poly.model.User;
import com.poly.repository.UserRepository;
import com.poly.service.EmailService;
import com.poly.service.OtpService;
import com.poly.service.PasswordEncode;
import com.poly.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("user/api/info")
public class UserInfoRestController {
    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncode passwordEncoder;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService mailService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @Valid User userDetails) {
        User updatedUser = userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        try {
            // Check if the username is being changed
            if (userDetails.getUsername() != null && !userDetails.getUsername().equals(updatedUser.getUsername())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không thể thay đổi tên tài khoản.");
            }

            // Check OTP verification status for email update
            if (userDetails.getEmail() != null && !userDetails.getEmail().equals(updatedUser.getEmail())) {
                boolean emailExists = userService.existsByEmail(userDetails.getEmail());
                if (emailExists) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email đã được sử dụng!!");
                }
                // Ensure user has verified OTP before allowing email update
                if (!otpService.isEmailVerifiedForUpdate(updatedUser.getId())) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OTP chưa được xác nhận cho cập nhật email hoặc đã hết hạn.");
                }
            }
            // Update other non-null fields
            updateNonNullFields(updatedUser, userDetails);
            otpService.resetEmailVerificationFlag(id);
            return ResponseEntity.ok(userService.save(updatedUser));

        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private void updateNonNullFields(User updatedUser, User userDetails) {
        if (userDetails.getUsername() != null) updatedUser.setUsername(userDetails.getUsername());
        if (userDetails.getPassword() != null) updatedUser.setPassword(userDetails.getPassword());  // Password hashing
        if (userDetails.getFullName() != null) updatedUser.setFullName(userDetails.getFullName());
        if (userDetails.getEmail() != null) updatedUser.setEmail(userDetails.getEmail());
        if (userDetails.getPhone() != null) updatedUser.setPhone(userDetails.getPhone());
        if (userDetails.getBirthday() != null) updatedUser.setBirthday(userDetails.getBirthday());
        updatedUser.setGender(userDetails.isGender());
        updatedUser.setStatus_user(userDetails.isStatus_user());
    }


    @PostMapping("/send-otp")
    public ResponseEntity<?> sendotpEmail(@RequestBody Map<String, Object> emailRQ) {
        String email = (String) emailRQ.get("emailRQ");
        User user = userRepository.findByEmail(email);
        if (user != null) {
            try {
                String otp = otpService.generateOtp(email);  // Generate OTP
                mailService.sendOtp(email, otp);             // Send OTP via email
                return ResponseEntity.ok("OTP đã được gửi tới email của bạn.");
            } catch (Exception e) {
                // Log the exception for debugging
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Có lỗi khi gửi OTP. Vui lòng thử lại.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Email không tồn tại trong hệ thống.");
        }
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtpForOtp(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("emailRQ");
        String otp = (String) request.get("otpRQ");
        User user = userRepository.findByEmail(email);

        if (user != null) {
            boolean isOtpValid = otpService.isOtpValid(email, otp);
            if (isOtpValid) {
                otpService.invalidateOtp(email);
                otpService.markEmailVerifiedForUpdate(user.getId());  // Đánh dấu là đã xác minh để cập nhật
                return ResponseEntity.ok("OTP hợp lệ. Bạn có thể tiến hành đổi Email mới.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP không hợp lệ hoặc đã hết hạn.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email không tồn tại trong hệ thống.");
        }
    }


    @PostMapping("/upload")
    public ResponseEntity<User> uploadImage(@RequestBody ImageUploadRequest request) {
        User user = userService.findById(request.getUserId()).get();
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Lưu URL của ảnh vào cơ sở dữ liệu
        user.setImage_user(request.getImageUrl());
        userService.save(user);
        return ResponseEntity.ok(user);
    }

}
