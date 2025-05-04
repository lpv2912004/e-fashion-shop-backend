package com.poly.admin;

import com.poly.dto.ColorDTO;
import com.poly.dto.SizeDTO;
import com.poly.dto.VariantProductDTO;
import com.poly.model.VariantProduct;
import com.poly.service.JwtService;
import com.poly.service.VariantProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/api/variant_products")
@CrossOrigin(origins = "*")  // Allow all origins
public class AdminVariantProductRestController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private VariantProductService variantProductService;

    @GetMapping
    public List<VariantProduct> getAllVariantProducts() {
        return variantProductService.findAll();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<VariantProductDTO>> getVariantByProductId(@PathVariable Integer productId) {
        List<VariantProduct> variants = variantProductService.findByProductId(productId);
        List<VariantProductDTO> variantDTOs = variants.stream().map(variant -> {
            // Map Color and Size objects to their respective DTOs
            ColorDTO colorDTO = variant.getColor() != null ? new ColorDTO(variant.getColor().getId(), variant.getColor().getColor_name()) : null;
            SizeDTO sizeDTO = variant.getSize() != null ? new SizeDTO(variant.getSize().getId(), variant.getSize().getName_size()) : null;

            return new VariantProductDTO(
                variant.getId(),
                variant.getQuantity(),
                variant.getPrice(),
                variant.getImage_variant(),
                variant.getStatus_VP(),
                colorDTO,    // Set the full ColorDTO object
                sizeDTO  // Set the full SizeDTO object
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(variantDTOs);
    }

    @GetMapping("/count-by-product/{productId}")
    public ResponseEntity<Long> countVariantsByProductId(@PathVariable Integer productId) {
        long count = variantProductService.countByProductId(productId);
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<VariantProduct> createVariantProduct(@RequestBody VariantProduct variantProduct, @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractClaim(jwtToken, claims -> claims.get("id_user", Integer.class));
        variantProduct.setCreatedBy(userId);
        VariantProduct savedVariant = variantProductService.save(variantProduct);
        return ResponseEntity.ok(savedVariant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VariantProduct> updateVariantProduct(@PathVariable Integer id, @RequestBody VariantProduct variantProductDetails, @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");

        // Giải mã token và lấy userId
        Integer userId = jwtService.extractClaim(jwtToken, claims -> claims.get("id_user", Integer.class));

        Optional<VariantProduct> variantProduct = variantProductService.findById(id);
        if (variantProduct.isPresent()) {
            VariantProduct updatedVariantProduct = variantProduct.get();
            updatedVariantProduct.setQuantity(variantProductDetails.getQuantity());
            updatedVariantProduct.setPrice(variantProductDetails.getPrice());
            updatedVariantProduct.setUpdatedBy(userId);
            updatedVariantProduct.setUpdated_date(variantProductDetails.getUpdated_date());
            updatedVariantProduct.setImage_variant(variantProductDetails.getImage_variant());
            updatedVariantProduct.setStatus_VP(variantProductDetails.getStatus_VP());
            updatedVariantProduct.setProduct(variantProductDetails.getProduct());
            updatedVariantProduct.setColor(variantProductDetails.getColor());
            updatedVariantProduct.setSize(variantProductDetails.getSize());

            return ResponseEntity.ok(variantProductService.save(updatedVariantProduct));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariantProduct(@PathVariable Integer id) {
        Optional<VariantProduct> variantProduct = variantProductService.findById(id);
        if (variantProduct.isPresent()) {
            variantProductService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
