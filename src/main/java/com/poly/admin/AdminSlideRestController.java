package com.poly.admin;

import com.poly.model.Slide;
import com.poly.model.User;
import com.poly.service.JwtService;
import com.poly.service.SlideService;
import com.poly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/api/slides")
@CrossOrigin(origins = "*")
public class AdminSlideRestController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SlideService slideService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<Slide>> getAllSlides() {
        try {
            List<Slide> slides = slideService.getAllSlides();
            return new ResponseEntity<>(slides, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API thêm ảnh
    @PostMapping("/add")
    public ResponseEntity<Slide> addSlide(@RequestBody Slide slide, @RequestHeader("Authorization") String token) {
        try {
            // Lấy userId từ token JWT
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractClaim(jwtToken, claims -> claims.get("id_user", Integer.class));

            // Tìm User từ userId
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Gán User vào Slide
            slide.setUser(user);

            // Lưu Slide vào cơ sở dữ liệu
            Slide newSlide = slideService.saveSlide(slide);
            return new ResponseEntity<>(newSlide, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Slide> updateSlideImage(@PathVariable("id") Integer id, @RequestBody Slide updatedSlide, @RequestHeader("Authorization") String token) {
        try {
            // Kiểm tra token để lấy userId (nếu cần thiết)
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractClaim(jwtToken, claims -> claims.get("id_user", Integer.class));

            // Tìm Slide trong cơ sở dữ liệu
            Optional<Slide> optionalSlide = slideService.getSlideById(id);
            if (optionalSlide.isPresent()) {
                Slide existingSlide = optionalSlide.get();

                // Cập nhật URL của ảnh mới
                existingSlide.setImage_SlideShow(updatedSlide.getImage_SlideShow());

                // Lưu Slide đã được cập nhật
                Slide updated = slideService.saveSlide(existingSlide);
                return new ResponseEntity<>(updated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // API xóa ảnh
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteSlide(@PathVariable("id") Integer id) {
        try {
            slideService.deleteSlideById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
