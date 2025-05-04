package com.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageUploadRequest {
    private int userId;
    private String imageUrl;
}
