package com.poly.user;


import com.poly.dto.ProductIdsRequest;
import com.poly.model.Like;
import com.poly.model.Product;
import com.poly.model.User;
import com.poly.repository.LikeRepository;
import com.poly.repository.ProductRepository;
import com.poly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("user/api/like")
public class UserLikeRestController {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}/{productId}")
    public Like getUserLikeStatus(@PathVariable Integer userId, @PathVariable Integer productId) {
        Optional<Like> existingLike = likeRepository.findByUser_IdAndProduct_Id(userId, productId);
        return existingLike.orElse(null); // Trả về đối tượng Like hoặc null nếu không có
    }
    // Endpoint để like hoặc unlike sản phẩm
    @PostMapping("/{userId}/{productId}")
    public Like likeOrUnlikeProduct(@PathVariable Integer userId, @PathVariable Integer productId) {
        Optional<Like> existingLike = likeRepository.findByUser_IdAndProduct_Id(userId, productId);

        if (existingLike.isPresent()) {
            // Nếu đã có, thì xóa nếu isLiked là true, hoặc đặt lại isLiked thành true nếu là false
            Like like = existingLike.get();
            like.setIsLiked(!like.getIsLiked());
            return likeRepository.save(like);
        } else {
            // Nếu chưa có, thì tạo mới
            Like like = new Like();
            like.setUser(userRepository.findById(userId).orElse(null));
            like.setProduct(productRepository.findById(productId).orElse(null));
            like.setIsLiked(true);
            return likeRepository.save(like);
        }
    }

    @GetMapping("/favorites/{userId}")
    public List<Product> getUserFavorites(@PathVariable Integer userId) {
        List<Like> likedProducts = likeRepository.findByUser_IdAndIsLikedTrue(userId);

        // Trả về danh sách sản phẩm mà người dùng đã thích
        return likedProducts.stream()
                .map(Like::getProduct)
                .collect(Collectors.toList());
    }

    @PostMapping("/unlike-multiple/{userId}")
    public List<Like> unlikeMultipleProducts(@PathVariable Integer userId, @RequestBody ProductIdsRequest request) {
        List<Integer> productIds = request.getProductIds(); // Lấy danh sách productIds từ DTO

        // Tìm tất cả các sản phẩm đã like với userId và nằm trong danh sách productIds
        List<Like> likesToUnlike = likeRepository.findByUser_IdAndProduct_IdInAndIsLikedTrue(userId, productIds);

        // Cập nhật trạng thái isLiked thành false cho tất cả sản phẩm đã được tìm thấy
        likesToUnlike.forEach(like -> like.setIsLiked(false));

        // Lưu lại các thay đổi
        return likeRepository.saveAll(likesToUnlike);
    }


}
