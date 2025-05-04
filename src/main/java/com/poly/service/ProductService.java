package com.poly.service;

import com.poly.model.Product;
import com.poly.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.poly.dto.BrandDTO;
import com.poly.dto.CategoryDTO;
import com.poly.dto.ColorDTO;
import com.poly.dto.FlashSaleDTO;
import com.poly.dto.ProductDTO;
import com.poly.dto.ProductVariantDTO;
import com.poly.dto.SizeDTO;
import com.poly.dto.VariantProductDTO;
import com.poly.dto.ActivitySaleDTO;
import com.poly.model.VariantProduct;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    public List<Product> getProductsCreatedToday() {
        return productRepository.findProductsCreatedToday();
    }

    // Phương thức chuyển đổi từ Product sang ProductDTO
    public static ProductDTO toProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setNameProd(product.getName_prod());
        dto.setDescription(product.getDescription());
        dto.setImageProd(product.getImage_prod());
        dto.setStatusProd(product.getStatus_prod());
        dto.setSumQuantity(product.getSum_quantity());

        // Ánh xạ Category
        if (product.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setNameCate(product.getCategory().getName_cate());
            dto.setCategory(categoryDTO);
        }

        // Ánh xạ Brand
        if (product.getBrand() != null) {
            BrandDTO brandDTO = new BrandDTO();
            brandDTO.setId(product.getBrand().getId());
            brandDTO.setNameBrand(product.getBrand().getName_brand());
            dto.setBrand(brandDTO);
        }

        // Ánh xạ Variants
        List<VariantProductDTO> variantDTOs = product.getVariantProds().stream()
            .map(ProductService::toVariantProductDTO)
            .collect(Collectors.toList());
        dto.setVariants(variantDTOs);

        return dto;
    }

    // Phương thức chuyển đổi từ VariantProduct sang VariantProductDTO
    public static VariantProductDTO toVariantProductDTO(VariantProduct variant) {
        VariantProductDTO dto = new VariantProductDTO();
        dto.setId(variant.getId());
        dto.setQuantity(variant.getQuantity());
        dto.setPrice(variant.getPrice());
        dto.setImageVariant(variant.getImage_variant());
        dto.setStatus_VP(variant.getStatus_VP());

        // Ánh xạ Color
        if (variant.getColor() != null) {
            ColorDTO colorDTO = new ColorDTO();
            colorDTO.setId(variant.getColor().getId());
            colorDTO.setColorName(variant.getColor().getColor_name());
            dto.setColor(colorDTO);
        }

        // Ánh xạ Size
        if (variant.getSize() != null) {
            SizeDTO sizeDTO = new SizeDTO();
            sizeDTO.setId(variant.getSize().getId());
            sizeDTO.setNameSize(variant.getSize().getName_size());
            dto.setSize(sizeDTO);
        }

        // Ánh xạ FlashSale
//        if (variant.getFlashSale() != null) {
//            FlashSaleDTO flashSaleDTO = new FlashSaleDTO();
//            flashSaleDTO.setId(variant.getFlashSale().getId());
//            flashSaleDTO.setName_FS(variant.getFlashSale().getName_FS());
//
//            // Ánh xạ ActivitySales nếu có
//            List<ActivitySaleDTO> activitySalesDTOs = variant.getFlashSale().getActivitySales().stream()
//                .map(activity -> new ActivitySaleDTO(
//                    activity.getId(), 
//                    activity.getDiscount_percent(),
//                    activity.getCreated_date(),
//                    activity.getExpiration_date()
//                )).collect(Collectors.toList());
//            flashSaleDTO.setActivitySales(activitySalesDTOs);
//
//            dto.setFlashSale(flashSaleDTO);
//        }

        return dto;
    }

    // Các phương thức của ProductService
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

	public List<ProductVariantDTO> getAllProductVariants() {
        return productRepository.getAllProductVariants();
	}
	
	public List<Map<String, Object>> getBestSellingProducts() {
        List<Object[]> results = productRepository.findBestSellingProducts();
        return results.stream().map(row -> {
            Map<String, Object> product = new HashMap<>();
            product.put("id", row[0]);
            product.put("name_prod", row[1]);
            product.put("image_prod", row[2]);
            product.put("totalSold", row[3]);
            return product;
        }).collect(Collectors.toList());
    }


}
