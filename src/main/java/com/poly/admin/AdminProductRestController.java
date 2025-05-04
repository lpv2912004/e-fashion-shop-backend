package com.poly.admin;

import com.poly.dto.ProductDTO;
import com.poly.model.Brand;
import com.poly.model.Category;
import com.poly.model.Product;
import com.poly.service.BrandService;
import com.poly.service.CategoryService;
import com.poly.service.JwtService;
import com.poly.service.ProductService;
import com.poly.service.VariantProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("admin/api/products")
@CrossOrigin(origins = "*")  // Allow all origins
public class AdminProductRestController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
@Autowired
private VariantProductService variantProductService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public List<Product> getAllProducts() {
        List<Product> products = productService.findAll();

        for (Product product : products) {
            // Kiểm tra xem sản phẩm có biến thể hay không
            boolean hasVariants = variantProductService.existsByProductId(product.getId());
            if (hasVariants) {
                // Nếu có biến thể, tính tổng số lượng tồn kho
                int totalQuantity = variantProductService.sumQuantityByProductId(product.getId());
                product.setSum_quantity(totalQuantity); // Cập nhật tổng số lượng tồn kho

                // Lưu lại sản phẩm với tổng số lượng đã cập nhật
                productService.save(product);
            }
        }

        return products; // Trả về danh sách sản phẩm đã cập nhật
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            ProductDTO productDTO = ProductService.toProductDTO(product.get());
            
            return ResponseEntity.ok(productDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//
//    @PostMapping
//    public ResponseEntity<Product> createProduct(@RequestBody Map<String, Object> productData, @RequestHeader("Authorization") String token) {
//        String nameProd = (String) productData.get("name_prod");
//        String description = (String) productData.get("description");
//        String imageProd = (String) productData.get("image_prod");
//        Integer brandId = Integer.parseInt((String) productData.get("brandId"));
//        Integer categoryId = Integer.parseInt((String) productData.get("categoryId"));
//        
//        // Giải mã token để lấy thông tin người dùng
//     // Lấy token từ Authorization header
//        String jwtToken = token.replace("Bearer ", "");
//
//        // Sử dụng jwtService để trích xuất username
//        String username = jwtService.extractUsername(jwtToken);
//
//        // Giải mã token và lấy userId trực tiếp từ claims thông qua jwtService
//        Integer userId = jwtService.extractClaim(jwtToken, claims -> claims.get("id_user", Integer.class));
//
//        // In ra userId nếu cần thiết
//        System.out.println("UserId from token: " + userId);
//
//
//
//
//        // Tìm Brand và Category theo ID
//        Brand brand = brandService.findById(brandId).orElseThrow(() -> new RuntimeException("Brand not found"));
//        Category category = categoryService.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
//
//        // Tạo đối tượng Product
//        Product product = new Product();
//        product.setName_prod(nameProd);
//        product.setDescription(description);
//        product.setImage_prod(imageProd);
//        product.setBrand(brand);
//        product.setCategory(category);
//        product.setStatus_prod(false); // Mặc định là 1 khi thêm mới
//        product.setSum_quantity(0); // Tạm thời là 0, sẽ cập nhật sau khi thêm biến thể
//        product.setCreated_by(userId); // Lấy từ token người dùng đã đăng nhập
//        product.setUpdated_by(userId); // Lấy từ token người dùng đã đăng nhập
//        product.setCreated_date(new Date());
////        product.setUpdated_date(new Date());
//
//        // Lưu sản phẩm
//        Product savedProduct = productService.save(product);
//
//        return ResponseEntity.ok(savedProduct);
//    }


//    @PostMapping
//    public ResponseEntity<Product> createProduct(@RequestBody Map<String, Object> productData, @RequestHeader("Authorization") String token) {
//        String nameProd = (String) productData.get("name_prod");
//        String description = (String) productData.get("description");
//        String imageProd = (String) productData.get("image_prod");
//        Integer brandId = Integer.parseInt((String) productData.get("brandId"));
//        Integer categoryId = Integer.parseInt((String) productData.get("categoryId"));
//        
//        // Giải mã token và lấy userId từ claims thông qua jwtService
//        String jwtToken = token.replace("Bearer ", "");
//        Integer userId = jwtService.extractClaim(jwtToken, claims -> claims.get("id_user", Integer.class));
//        
//        // In ra userId nếu cần thiết
//        System.out.println("UserId from token: " + userId);
//
//        // Tìm Brand và Category theo ID
//        Brand brand = brandService.findById(brandId).orElseThrow(() -> new RuntimeException("Brand not found"));
//        Category category = categoryService.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
//
//        // Tạo đối tượng Product
//        Product product = new Product();
//        product.setName_prod(nameProd);
//        product.setDescription(description);
//        product.setImage_prod(imageProd);
//        product.setBrand(brand);
//        product.setCategory(category);
//        product.setStatus_prod(false); // Mặc định là false khi thêm mới
//        product.setSum_quantity(0); // Tạm thời là 0, sẽ cập nhật sau khi thêm biến thể
//        product.setCreated_by(userId);
//        product.setUpdated_by(userId);
//        product.setCreated_date(new Date());
//
//        // Lưu sản phẩm
//        Product savedProduct = productService.save(product);
//
//        // Sau khi sản phẩm được lưu, cập nhật số lượng từ các biến thể nếu có
//        Integer productId = savedProduct.getId();
//        int totalQuantity = variantProductService.sumQuantityByProductId(productId); // Tính tổng số lượng biến thể
//        savedProduct.setSum_quantity(totalQuantity); // Cập nhật tổng số lượng
//
//        // Lưu lại sản phẩm với tổng số lượng đã cập nhật
//        productService.save(savedProduct);
//
//        return ResponseEntity.ok(savedProduct);
//    }
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Map<String, Object> productData, @RequestHeader("Authorization") String token) {
        String nameProd = (String) productData.get("name_prod");
        String description = (String) productData.get("description");
        String imageProd = (String) productData.get("image_prod");
        Integer brandId = Integer.parseInt((String) productData.get("brandId"));
        Integer categoryId = Integer.parseInt((String) productData.get("categoryId"));
        
        // Giải mã token và lấy userId từ claims thông qua jwtService
        String jwtToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractClaim(jwtToken, claims -> claims.get("id_user", Integer.class));
        
        // In ra userId nếu cần thiết
        System.out.println("UserId from token: " + userId);

        // Tìm Brand và Category theo ID
        Brand brand = brandService.findById(brandId).orElseThrow(() -> new RuntimeException("Brand not found"));
        Category category = categoryService.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));

        // Tạo đối tượng Product
        Product product = new Product();
        product.setName_prod(nameProd);
        product.setDescription(description);
        product.setImage_prod(imageProd);
        product.setBrand(brand);
        product.setCategory(category);
        product.setStatus_prod(false); // Mặc định là false khi thêm mới
        product.setSum_quantity(0); // Tạm thời là 0, sẽ cập nhật sau nếu có biến thể
        product.setCreated_by(userId);
        product.setUpdated_by(userId);
        product.setCreated_date(new Date());

        // Lưu sản phẩm
        Product savedProduct = productService.save(product);

        // Lấy ID của sản phẩm sau khi lưu
        Integer productId = savedProduct.getId();

        // Kiểm tra xem sản phẩm có biến thể hay không
        boolean hasVariants = variantProductService.existsByProductId(productId); // Giả định bạn có method này trong service

        if (hasVariants) {
            // Nếu có biến thể, tính tổng số lượng
            int totalQuantity = variantProductService.sumQuantityByProductId(productId); // Tính tổng số lượng biến thể
            savedProduct.setSum_quantity(totalQuantity); // Cập nhật tổng số lượng

            // Lưu lại sản phẩm với tổng số lượng đã cập nhật
            productService.save(savedProduct);
        }

        return ResponseEntity.ok(savedProduct);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product productDetails, @RequestHeader("Authorization") String token) {
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            Product existingProduct = productOptional.get();

            // Kiểm tra từng trường và chỉ cập nhật nếu dữ liệu không phải là null
            if (productDetails.getName_prod() != null) {
                existingProduct.setName_prod(productDetails.getName_prod());
            }

            if (productDetails.getDescription() != null) {
                existingProduct.setDescription(productDetails.getDescription());
            }

            if (productDetails.getImage_prod() != null) {
                existingProduct.setImage_prod(productDetails.getImage_prod());
            }

            if (productDetails.getBrand() != null) {
                existingProduct.setBrand(productDetails.getBrand());
            }

            if (productDetails.getCategory() != null) {
                existingProduct.setCategory(productDetails.getCategory());
            }

            // Cập nhật trạng thái sản phẩm nếu giá trị hợp lệ
            existingProduct.setStatus_prod(productDetails.getStatus_prod() != null ? productDetails.getStatus_prod() : existingProduct.getStatus_prod());

//            // Tính toán tổng số lượng từ các biến thể (giữ nguyên nếu không thay đổi)
//            int totalQuantity = variantProductService.sumQuantityByProductId(id);
//            existingProduct.setSum_quantity(totalQuantity);
            if (variantProductService.existsByProductId(id)) { // Kiểm tra nếu sản phẩm có biến thể
                int totalQuantity = variantProductService.sumQuantityByProductId(id);
                existingProduct.setSum_quantity(totalQuantity);
            }
            // Cập nhật người dùng và thời gian cập nhật
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractClaim(jwtToken, claims -> claims.get("id_user", Integer.class));
            existingProduct.setUpdated_by(userId);
            existingProduct.setUpdated_date(new Date());

            // Lưu sản phẩm đã được cập nhật
            Product updatedProduct = productService.save(existingProduct);
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/{productId}/has-flash-sale-variants")
//    public ResponseEntity<Boolean> hasFlashSaleVariants(@PathVariable Integer productId) {
//        boolean hasFlashSale = variantProductService.hasFlashSaleVariants(productId);
//        return ResponseEntity.ok(hasFlashSale);
//    }



//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
//        productService.deleteById(id);
//        return ResponseEntity.ok().build();
//    }
}
