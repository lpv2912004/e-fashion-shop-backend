package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.dto.ProductVariantDTO;
import com.poly.model.Order;
import com.poly.model.Product;
import com.poly.model.User;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	@Query("SELECT p.id, p.name_prod, p.image_prod, MIN(v.price) " + "FROM Product p " + "JOIN p.variantProds v "
			+ "WHERE LOWER(REPLACE(p.name_prod, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) "
			+ "GROUP BY p.id, p.name_prod, p.image_prod")
	List<Object[]> searchProductsWithLowestPrice(@Param("keyword") String keyword);

	@Query("SELECT new com.poly.dto.ProductVariantDTO(p.id, p.name_prod, p.image_prod, p.description, p.status_prod, p.sum_quantity, c.name_cate, b.name_brand, "
			+ "vp.id, vp.quantity, vp.price, vp.image_variant, cl.color_name, s.name_size) " + "FROM Product p "
			+ "JOIN p.category c " + "JOIN p.brand b " + "JOIN p.variantProds vp " + "JOIN vp.color cl "
			+ "JOIN vp.size s")
	List<ProductVariantDTO> getAllProductVariants();

	@Query("SELECT DISTINCT p " + "FROM Product p " + "JOIN p.variantProds vp " + "WHERE vp.status_VP = 1 "
			+ "ORDER BY p.updated_date DESC") // sx
	List<Product> findAllWithActiveVariants();

	@Query("SELECT p.id, p.name_prod, p.image_prod, SUM(od.quantity) as totalSold " + "FROM Product p "
			+ "JOIN p.variantProds vp " + "JOIN vp.orderDetails od " + "WHERE vp.status_VP = 1 " + // Chỉ lấy biến thể
																									// đang bán
			"GROUP BY p.id, p.name_prod, p.image_prod " + "ORDER BY totalSold DESC")
	List<Object[]> findBestSellingProducts();

	@Query("SELECT new com.poly.dto.ProductVariantDTO("
			+ "p.id, p.name_prod, p.image_prod, p.description, p.status_prod, p.sum_quantity, "
			+ "c.name_cate, b.name_brand, "
			+ "vp.id, vp.quantity, vp.price, vp.image_variant, cl.color_name, s.name_size) " + "FROM Product p "
			+ "JOIN p.category c " + "JOIN p.brand b " + "JOIN p.variantProds vp " + "JOIN vp.color cl "
			+ "JOIN vp.size s " + "WHERE vp.status_VP = 1")
	List<ProductVariantDTO> findActiveProductsWithVariants();

	@Query(value = """
			SELECT DATENAME(weekday, o.created_date) AS day, SUM(o.total_cash) AS total
			FROM Orders o
			WHERE DATEPART(week, o.created_date) = DATEPART(week, GETDATE())
			AND YEAR(o.created_date) = YEAR(GETDATE())
			GROUP BY DATENAME(weekday, o.created_date), DATEPART(weekday, o.created_date)
			ORDER BY DATEPART(weekday, o.created_date) ASC
			""", nativeQuery = true)
	List<Object[]> findWeeklySales();

	@Query("SELECT p FROM Product p WHERE CAST(p.created_date AS date) = CAST(GETDATE() AS date)")
	List<Product> findProductsCreatedToday();

	@Query("SELECT u FROM User u WHERE CAST(u.created_date AS date) = CAST(GETDATE() AS date)")
	List<User> findUsersCreatedToday();

	@Query("SELECT o FROM Order o WHERE CAST(o.created_date AS date) = CAST(GETDATE() AS date) AND o.state.id = 9")
	List<Order> findOrdersTodayWithStateDelivered();

	@Query("SELECT SUM(o.total_cash) FROM Order o WHERE CAST(o.created_date AS date) = CAST(GETDATE() AS date) AND o.state.id = 9")
	Double findTotalRevenueTodayWithStateDelivered();

}
