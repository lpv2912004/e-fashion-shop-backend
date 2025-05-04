package com.poly.admin;

import com.poly.model.Size;
import com.poly.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin/api/sizes")
@CrossOrigin(origins = "*")  // Allow all origins
public class AdminSizeRestController {

    @Autowired
    private SizeService sizeService;

    @GetMapping
    public List<Size> getAllSizes() {
        return sizeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Size> getSizeById(@PathVariable Integer id) {
        Optional<Size> size = sizeService.findById(id);
        if (size.isPresent()) {
            return ResponseEntity.ok(size.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Size> createSize(@RequestBody Size size) {
        return ResponseEntity.ok(sizeService.save(size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Size> updateSize(@PathVariable Integer id, @RequestBody Size sizeDetails) {
        Optional<Size> size = sizeService.findById(id);
        if (size.isPresent()) {
            Size updatedSize = size.get();
            updatedSize.setName_size(sizeDetails.getName_size());
            return ResponseEntity.ok(sizeService.save(updatedSize));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSize(@PathVariable Integer id) {
        sizeService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
