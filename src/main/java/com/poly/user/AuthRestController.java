//package com.poly.user;
//
//import com.fasterxml.jackson.core.sym.Name;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthException;
//import com.google.firebase.auth.FirebaseToken;
//import com.poly.dto.FacebookUser;
//import com.poly.model.Role;
//import com.poly.repository.RoleRepository;
//import com.poly.service.EmailUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import com.poly.dto.UserDTO;
//import com.poly.model.User;
//import com.poly.repository.UserRepository;
//import com.poly.service.JwtService;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//@RequestMapping("/api/v1")
//public class AuthRestController {
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtService jwtService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private EmailUserService emailUserService;
//    @Autowired
//    RoleRepository roleRepository;
//
//    @Autowired
//    @Qualifier("firebaseAuth")
//    private FirebaseAuth firebaseAuth;
//
////    @PostMapping("/login")
////    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDTO authenticationRequest) throws Exception {
////        try {
////            // Xác thực tài khoản
////            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
////                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
////
////            // Lấy thông tin người dùng từ UserDetailsService
////            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
////
////            // Kiểm tra trạng thái tài khoản
////            User user = userRepository.findByUsername(authenticationRequest.getUsername());
////            if (!user.isStatus_user()) {
////                return ResponseEntity.status(401).body("Tài khoản của bạn đã bị vô hiệu hóa.");
////            }
////
////            // Tạo token JWT
////            final String jwt = jwtService.generateToken(userDetails);
////
////            // Trả về JWT trong phản hồi JSON
////            return ResponseEntity.ok(new AuthenticationResponse(jwt));
////
////        } catch (BadCredentialsException e) {
////            e.printStackTrace();
////            // Nếu thông tin đăng nhập không đúng, trả về mã 401
////            return ResponseEntity.ok("Tài khoản hoặc mật khẩu không chính xác");
////
////        } catch (Exception e) {
////            e.printStackTrace();
////            // Xử lý các lỗi khác, trả về mã 500 (Internal Server Error)
////            return ResponseEntity.ok("Đã có lỗi xảy ra");
////
////        }
////    }
//@PostMapping("/login")
//public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDTO authenticationRequest) throws Exception {
//    try {
//        // Xác thực tài khoản
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                authenticationRequest.getUsername(), authenticationRequest.getPassword()));
//
//        // Lấy thông tin người dùng từ UserDetailsService
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//
//        // Kiểm tra trạng thái tài khoản
//        User user = userRepository.findByUsername(authenticationRequest.getUsername());
//        if (!user.isStatus_user()) {
//            // Gửi email thông báo tài khoản bị vô hiệu hóa
//            String subject = "Thông báo tài khoản bị vô hiệu hóa";
//            String content = "Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ với quản trị viên để biết thêm chi tiết.";
//            emailUserService.sendPlainTextEmail(user.getEmail(), subject, content); // Gửi email
//
//            return ResponseEntity.status(401).body("Tài khoản của bạn đã bị vô hiệu hóa.");
//        }
//
//        // Tạo token JWT
//        final String jwt = jwtService.generateToken(userDetails);
//
//        // Trả về JWT trong phản hồi JSON
//        return ResponseEntity.ok(new AuthenticationResponse(jwt));
//
//    } catch (BadCredentialsException e) {
//        e.printStackTrace();
//        // Nếu thông tin đăng nhập không đúng, trả về mã 401
//        return ResponseEntity.ok("Tài khoản hoặc mật khẩu không chính xác");
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        // Xử lý các lỗi khác, trả về mã 500 (Internal Server Error)
//        return ResponseEntity.ok("Đã có lỗi xảy ra");
//    }
//}
//
//
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
//        String authorizationHeader = request.getHeader("Authorization");
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);
//            String username = jwtService.extractUsername(token);
//            jwtService.deleteToken(username);
//
//            Cookie cookie = new Cookie("Authorization", null);
//            cookie.setHttpOnly(true);
//            cookie.setSecure(true);
//            cookie.setPath("/");
//            cookie.setMaxAge(0);
//            response.addCookie(cookie);
//        }
//
//        response.setStatus(HttpServletResponse.SC_OK);
//        return ResponseEntity.ok("Logout successful");
//    }
//
//    @PostMapping("/google-login")
//    public ResponseEntity<?> googleLogin(@RequestBody String idToken) {
//        try {
//            // Xác thực `idToken` từ Google
//
////            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
//        	  FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
//            String email = decodedToken.getEmail();
//            String name = decodedToken.getName();
//
//            // Kiểm tra người dùng trong cơ sở dữ liệu
//            User user = userRepository.findByEmail(email);
//            if (user == null) {
//                // Tạo người dùng mới nếu chưa tồn tại
//                user = new User();
//                Role role = roleRepository.findById(2).get();
//                user.setUsername(email);  // Hoặc một username tùy chỉnh
//                user.setEmail(email);
//                user.setFullName(name);
//                user.setStatus_user(true);
//                user.setRole(role);
//                // Tạo mật khẩu ngẫu nhiên và mã hóa nó
//                String randomPassword = "defaultPassword123"; // Bạn có thể sử dụng mật khẩu ngẫu nhiên hoặc chuỗi mặc định
//                user.setPassword(passwordEncoder.encode(randomPassword));
//                userRepository.save(user);
//            }
//
//            // Tạo JWT cho người dùng
//            String jwt = jwtService.generateToken(user);
//
//            // Trả về JWT cho frontend
//            return ResponseEntity.ok(new AuthenticationResponse(jwt));
//
//        } catch (FirebaseAuthException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google ID token");
//        }
//    }
//
//    @PostMapping("/facebook-login")
//    public ResponseEntity<?> facebookLogin(@RequestBody String facebookAccessToken) {
//        try {
//            // Verify the access token by calling the Facebook Graph API
//            String facebookUrl = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + facebookAccessToken;
//            RestTemplate restTemplate = new RestTemplate();
//            FacebookUser facebookUser = restTemplate.getForObject(facebookUrl, FacebookUser.class);
//
//            if (facebookUser == null || facebookUser.getEmail() == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Facebook access token");
//            }
//
//            // Check if user exists in the database
//            User user = userRepository.findByEmail(facebookUser.getEmail());
//            if (user == null) {
//                // Create new user if not found
//                user = new User();
//                Role role = roleRepository.findById(2).get();
//                user.setUsername(facebookUser.getEmail());
//                user.setEmail(facebookUser.getEmail());
//                user.setFullName(facebookUser.getName());
//                user.setStatus_user(true);
//                user.setRole(role);
//
//                // Set a default or random password and encode it
//                String randomPassword = "defaultPassword123";
//                user.setPassword(passwordEncoder.encode(randomPassword));
//                userRepository.save(user);
//            }
//
//            // Generate JWT for the user
//            String jwt = jwtService.generateToken(user);
//            System.out.println("Generated JWT: " + jwt); // Debugging
//
//            // Return JWT to frontend
//            return ResponseEntity.ok(new AuthenticationResponse(jwt));
//
//        } catch (RestClientException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to verify Facebook access token");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during Facebook login");
//        }
//    }
//
//
//}
//
//class AuthenticationResponse {
//    private final String jwt;
//
//    public AuthenticationResponse(String jwt) {
//        this.jwt = jwt;
//    }
//
//    public String getJwt() {
//        return jwt;
//    }
//}
//

package com.poly.user;

import com.fasterxml.jackson.core.sym.Name;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.poly.dto.FacebookUser;
import com.poly.model.Role;
import com.poly.repository.RoleRepository;
import com.poly.service.EmailUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.poly.dto.UserDTO;
import com.poly.model.User;
import com.poly.repository.UserRepository;
import com.poly.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1")
public class AuthRestController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUserService emailUserService;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    @Qualifier("firebaseAuth")
    private FirebaseAuth firebaseAuth;


@PostMapping("/login")
public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDTO authenticationRequest) throws Exception {
    try {
        // Xác thực tài khoản
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        // Lấy thông tin người dùng từ UserDetailsService
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        // Kiểm tra trạng thái tài khoản
        User user = userRepository.findByUsername(authenticationRequest.getUsername());
        if (!user.isStatus_user()) {
            // Thêm thông tin cửa hàng (có thể lấy từ cơ sở dữ liệu hoặc cấu hình tĩnh)
        	
        	String customerName = user.getFullName();  // Tên người dùng
            String shopName = "Shop Name";  // Tên cửa hàng (có thể lấy từ cấu hình hoặc cơ sở dữ liệu)
            String shopAddress = "Shop Address";  // Địa chỉ cửa hàng
            String shopContact = "Contact Info";  // Thông tin liên hệ (số điện thoại hoặc email)

            // Gửi email thông báo tài khoản bị vô hiệu hóa
            String subject = "Thông báo tài khoản bị vô hiệu hóa";
            String content = "Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ với quản trị viên để biết thêm chi tiết.";
            emailUserService.sendUserNotificationEmail(user.getEmail(), subject, customerName, shopName,
                    shopAddress, shopContact, content); // Gửi email

            return ResponseEntity.status(401).body("Tài khoản của bạn đã bị vô hiệu hóa.");
        }

        // Tạo token JWT
        final String jwt = jwtService.generateToken(userDetails);

        // Trả về JWT trong phản hồi JSON
        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    } catch (BadCredentialsException e) {
        e.printStackTrace();
        // Nếu thông tin đăng nhập không đúng, trả về mã 401
        return ResponseEntity.ok("Tài khoản hoặc mật khẩu không chính xác");

    } catch (Exception e) {
        e.printStackTrace();
        // Xử lý các lỗi khác, trả về mã 500 (Internal Server Error)
        return ResponseEntity.ok("Đã có lỗi xảy ra");
    }
}




    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);
            jwtService.deleteToken(username);

            Cookie cookie = new Cookie("Authorization", null);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return ResponseEntity.ok("Logout successful");
    }


@PostMapping("/google-login")
public ResponseEntity<?> googleLogin(@RequestBody String idToken) {
    try {
        // Xác thực `idToken` từ Google
        FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
        String email = decodedToken.getEmail();
        String name = decodedToken.getName();

        // Kiểm tra người dùng trong cơ sở dữ liệu
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // Tạo người dùng mới nếu chưa tồn tại
            user = new User();
            
            Role role = roleRepository.findById(2).get();
            user.setUsername(email);  // Hoặc một username tùy chỉnh
            user.setEmail(email);
            user.setFullName(name);
            user.setStatus_user(true);
            user.setRole(role);
            // Tạo mật khẩu ngẫu nhiên và mã hóa nó
            String randomPassword = "defaultPassword123"; // Bạn có thể sử dụng mật khẩu ngẫu nhiên hoặc chuỗi mặc định
            user.setPassword(passwordEncoder.encode(randomPassword));
            userRepository.save(user);
        }

        // Kiểm tra trạng thái tài khoản
        if (!user.isStatus_user()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản của bạn đã bị vô hiệu hóa.");
        }

        // Tạo JWT cho người dùng
        String jwt = jwtService.generateToken(user);

        // Trả về JWT cho frontend
        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    } catch (FirebaseAuthException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google ID token");
    }
}

    @PostMapping("/facebook-login")
    public ResponseEntity<?> facebookLogin(@RequestBody String facebookAccessToken) {
        try {
            // Verify the access token by calling the Facebook Graph API
            String facebookUrl = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + facebookAccessToken;
            RestTemplate restTemplate = new RestTemplate();
            FacebookUser facebookUser = restTemplate.getForObject(facebookUrl, FacebookUser.class);

            if (facebookUser == null || facebookUser.getEmail() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Facebook access token");
            }

            // Check if user exists in the database
            User user = userRepository.findByEmail(facebookUser.getEmail());
            if (user == null) {
                // Create new user if not found
                user = new User();
                Role role = roleRepository.findById(2).get();
                user.setUsername(facebookUser.getEmail());
                user.setEmail(facebookUser.getEmail());
                user.setFullName(facebookUser.getName());
                user.setStatus_user(true);
                user.setRole(role);

                // Set a default or random password and encode it
                String randomPassword = "defaultPassword123";
                user.setPassword(passwordEncoder.encode(randomPassword));
                userRepository.save(user);
            }

            // Kiểm tra trạng thái tài khoản
            if (!user.isStatus_user()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản của bạn đã bị vô hiệu hóa.");
            }

            // Generate JWT for the user
            String jwt = jwtService.generateToken(user);
            
         // Return JWT to frontend
            return ResponseEntity.ok(new AuthenticationResponse(jwt));

        } catch (RestClientException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to verify Facebook access token");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during Facebook login");
        }
    }


}

class AuthenticationResponse {
    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
