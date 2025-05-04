package com.poly.admin;

import com.poly.model.Brand;
import com.poly.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin/api/brands")
@CrossOrigin(origins = "*")
public class AdminBrandRestController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public List<Brand> getAllBrands() {
        return brandService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Integer id) {
        Optional<Brand> brand = brandService.findById(id);
        if (brand.isPresent()) {
            return ResponseEntity.ok(brand.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Brand> createBrand(@RequestBody Brand brand) {
        return ResponseEntity.ok(brandService.save(brand));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Integer id, @RequestBody Brand brandDetails) {
        Optional<Brand> brand = brandService.findById(id);
        if (brand.isPresent()) {
            Brand updatedBrand = brand.get();
            updatedBrand.setName_brand(brandDetails.getName_brand());
            updatedBrand.setPhone(brandDetails.getPhone());
            updatedBrand.setEmail(brandDetails.getEmail());
            updatedBrand.setAddress(brandDetails.getAddress());
            updatedBrand.setDescription(brandDetails.getDescription());

            // Update the image URL if it exists
            if (brandDetails.getImage_brand() != null && !brandDetails.getImage_brand().isEmpty()) {
                updatedBrand.setImage_brand(brandDetails.getImage_brand());
            }

            return ResponseEntity.ok(brandService.save(updatedBrand));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Integer id) {
        brandService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
