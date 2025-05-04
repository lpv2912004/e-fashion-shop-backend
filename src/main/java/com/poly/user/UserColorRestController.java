package com.poly.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poly.model.Category;
import com.poly.model.Color;
import com.poly.service.CategoryService;
import com.poly.service.ColorService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("user/api/colors")
public class UserColorRestController {

    @Autowired
    private ColorService colorService;

    @GetMapping
    public ResponseEntity<List<Color>> getAllCategories() {
        List<Color> colors = colorService.findAll();
        return ResponseEntity.ok(colors);
    }
}
