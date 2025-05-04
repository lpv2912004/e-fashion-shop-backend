package com.poly.repository;

import com.poly.model.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUser_IdAndProduct_Id(Integer userId, Integer productId);
    List<Like> findByUser_IdAndIsLikedTrue(Integer userId);

    // Đếm số lượt thích của một sản phẩm cụ thể
    long countByProduct_IdAndIsLikedTrue(Integer productId);

    // Thêm hàm này để tìm các sản phẩm dựa trên danh sách productIds
    List<Like> findByUser_IdAndProduct_IdInAndIsLikedTrue(Integer userId, List<Integer> productIds);
}
