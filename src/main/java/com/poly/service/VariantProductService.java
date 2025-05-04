package com.poly.service;

import com.poly.model.VariantProduct;
import com.poly.repository.VariantProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VariantProductService {

    @Autowired
    private VariantProductRepository variantProductRepository;
//    public boolean hasFlashSaleVariants(Integer productId) {
//        List<VariantProduct> variants = findByProductId(productId);
//        return variants.stream().anyMatch(variant -> variant.getFlashSale() != null);
//    }
    public List<VariantProduct> findAll() {
        return variantProductRepository.findAll();
    }

    public Optional<VariantProduct> findById(Integer id) {
        return variantProductRepository.findById(id);
    }

    public Optional<VariantProduct> findByProductAndColorAndSize(Integer productId, Integer colorId, Integer sizeId) {
        return variantProductRepository.findByProduct_IdAndColor_IdAndSize_Id(productId, colorId, sizeId);
    }

    public VariantProduct save(VariantProduct variantProduct) {
        return variantProductRepository.save(variantProduct);
    }

    public void deleteById(Integer id) {
        variantProductRepository.deleteById(id);
    }
    public List<VariantProduct> findByProductId(Integer productId) {
        return variantProductRepository.findByProduct_Id(productId);
    }
    public long countByProductId(Integer productId) {
        return variantProductRepository.countByProductId(productId);
    }
    public int sumQuantityByProductId(Integer productId) {
        return variantProductRepository.sumQuantityByProductId(productId);
    }
    public boolean existsByProductId(Integer productId) {
        return variantProductRepository.countByProductId(productId) > 0;
    }
}
