package com.poly.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.model.Category;
import com.poly.model.Size;
import com.poly.service.CategoryService;
import com.poly.service.SizeService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("user/api/sizes")
public class UserSizeRestController {
    @Autowired
    private SizeService sizeService;

    @GetMapping
    public ResponseEntity<List<Size>> findAll() {
        List<Size> sizes = sizeService.findAll();
        return ResponseEntity.ok(sizes);
    }
}
