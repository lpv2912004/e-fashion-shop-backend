package com.poly.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
    private Integer id;
    private String createdDate;
    private Integer numberStar;
    private String productName; // Chỉ trả về tên sản phẩm thay vì toàn bộ Product entity
    private String userName; // Chỉ trả về tên người dùng thay vì toàn bộ User entity
    private String content;
}
