//package com.poly.admin;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import com.poly.service.EmailUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.poly.model.User;
//import com.poly.repository.AddressRepository;
//
//import com.poly.service.UserService;
//
//@RestController
//@RequestMapping("admin/api/users")
//@CrossOrigin(origins = "*")
//public class AdminUserRestController {
//	@Autowired
//    private UserService userService;
//
//    @Autowired
//    private EmailUserService emailUserService;
//
//    @Autowired
//    AddressRepository addressRepository;
//
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userService.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
//        Optional<User> user = userService.findById(id);
//        if (user.isPresent()) {
//            return ResponseEntity.ok(user.get());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PostMapping
//    public User createUser(@RequestBody User user) {
//        return userService.save(user);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
//        Optional<User> user = userService.findById(id);
//        if (user.isPresent()) {
//            User updatedUser = user.get();
////            updatedUser.setUsername(userDetails.getUsername());
////            updatedUser.setPassword(userDetails.getPassword());
//            updatedUser.setFullName(userDetails.getFullName());
//            updatedUser.setGender(userDetails.isGender());
////            updatedUser.setEmail(userDetails.getEmail());
////            updatedUser.setPhone(userDetails.getPhone());
//            updatedUser.setBirthday(userDetails.getBirthday());
//            updatedUser.setStatus_user(userDetails.isStatus_user());
//
//            // Thêm logic để cập nhật ảnh
//            if (userDetails.getImage_user() != null) {
//                updatedUser.setImage_user(userDetails.getImage_user());
//            }
//
//            return ResponseEntity.ok(userService.save(updatedUser));
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
////
////    @PutMapping("/status/{id}")
////    public ResponseEntity<User> updateUserStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> statusUpdate) {
////        Optional<User> user = userService.findById(id);
////        if (user.isPresent()) {
////            User updatedUser = user.get();
////            updatedUser.setStatus_user(statusUpdate.get("status_user")); // Only update the status_user
////            return ResponseEntity.ok(userService.save(updatedUser));
////        } else {
////            return ResponseEntity.notFound().build();
////        }
////    }
//
//
//    @PutMapping("/status/{id}")
//    public ResponseEntity<User> updateUserStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> statusUpdate) {
//        Optional<User> user = userService.findById(id);
//        if (user.isPresent()) {
//            User updatedUser = user.get();
//            updatedUser.setStatus_user(statusUpdate.get("status_user")); // Cập nhật trạng thái
//            userService.save(updatedUser);
//
//            // Nếu trạng thái là vô hiệu hóa, gửi email thông báo
//            if (!updatedUser.isStatus_user()) {
//                String subject = "Thông báo tài khoản bị vô hiệu hóa";
//                String content = "Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ với quản trị viên để biết thêm chi tiết.";
//                emailUserService.sendPlainTextEmail(updatedUser.getEmail(), subject, content);
//            }
//            // Nếu trạng thái là kích hoạt, gửi email thông báo
//            else {
//                String subject = "Thông báo tài khoản đã được kích hoạt";
//                String content = "Tài khoản của bạn đã được kích hoạt. Bạn có thể đăng nhập và sử dụng dịch vụ. Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.";
//                emailUserService.sendPlainTextEmail(updatedUser.getEmail(), subject, content);
//            }
//
//            return ResponseEntity.ok(updatedUser);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
//        userService.deleteById(id);
//        return ResponseEntity.ok().build();
//    }
//}
//


package com.poly.admin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.poly.service.EmailUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.model.User;
import com.poly.repository.AddressRepository;

import com.poly.service.UserService;

@RestController
@RequestMapping("admin/api/users")
@CrossOrigin(origins = "*")
public class AdminUserRestController {
	@Autowired
    private UserService userService;

    @Autowired
    private EmailUserService emailUserService;

    @Autowired
    AddressRepository addressRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User updatedUser = user.get();
//            updatedUser.setUsername(userDetails.getUsername());
//            updatedUser.setPassword(userDetails.getPassword());
            updatedUser.setFullName(userDetails.getFullName());
            updatedUser.setGender(userDetails.isGender());
//            updatedUser.setEmail(userDetails.getEmail());
//            updatedUser.setPhone(userDetails.getPhone());
            updatedUser.setBirthday(userDetails.getBirthday());
            updatedUser.setStatus_user(userDetails.isStatus_user());

            // Thêm logic để cập nhật ảnh
            if (userDetails.getImage_user() != null) {
                updatedUser.setImage_user(userDetails.getImage_user());
            }

            return ResponseEntity.ok(userService.save(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/status/{id}")
    public ResponseEntity<User> updateUserStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> statusUpdate) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setStatus_user(statusUpdate.get("status_user")); // Cập nhật trạng thái
            userService.save(updatedUser);

            // Thêm thông tin cửa hàng (có thể lấy từ cơ sở dữ liệu hoặc cấu hình tĩnh)
            String customerName = updatedUser.getFullName();  // Tên người dùng
            String shopName = "Shop Maou";  // Tên cửa hàng (có thể lấy từ cấu hình hoặc cơ sở dữ liệu)
            String shopAddress = "150/8 Nguyễn Duy Cung, Phường 12, Tp.Cần thơ";  // Địa chỉ cửa hàng
            String shopContact = "19001393";  // Thông tin liên hệ (số điện thoại hoặc email)

            // Nếu trạng thái là vô hiệu hóa, gửi email thông báo
            if (!updatedUser.isStatus_user()) {
                String subject = "Thông báo tài khoản bị vô hiệu hóa";
                String content = "Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ với quản trị viên để biết thêm chi tiết.";
                emailUserService.sendUserNotificationEmail(updatedUser.getEmail(), subject, customerName, shopName,
                        shopAddress, shopContact, content);
            }
            // Nếu trạng thái là kích hoạt, gửi email thông báo
            else {
                String subject = "Thông báo tài khoản đã được kích hoạt";
                String content = "Tài khoản của bạn đã được kích hoạt. Bạn có thể đăng nhập và sử dụng dịch vụ. Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.";
                emailUserService.sendUserNotificationEmail(updatedUser.getEmail(), subject, customerName, shopName,
                        shopAddress, shopContact, content);
            }

            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
