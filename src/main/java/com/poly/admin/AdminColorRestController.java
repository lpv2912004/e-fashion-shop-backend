package com.poly.admin;

import com.poly.model.Color;
import com.poly.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin/api/colors")
@CrossOrigin(origins = "*")  // Allow all origins
public class AdminColorRestController {

    @Autowired
    private ColorService colorService;

    @GetMapping
    public List<Color> getAllColors() {
        return colorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Color> getColorById(@PathVariable Integer id) {
        Optional<Color> color = colorService.findById(id);
        if (color.isPresent()) {
            return ResponseEntity.ok(color.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Color> createColor(@RequestBody Color color) {
        return ResponseEntity.ok(colorService.save(color));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Color> updateColor(@PathVariable Integer id, @RequestBody Color colorDetails) {
        Optional<Color> color = colorService.findById(id);
        if (color.isPresent()) {
            Color updatedColor = color.get();
            updatedColor.setColor_name(colorDetails.getColor_name());
            return ResponseEntity.ok(colorService.save(updatedColor));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColor(@PathVariable Integer id) {
        colorService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
