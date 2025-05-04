package com.poly.admin;

import com.poly.model.Order;
import com.poly.model.Product;
import com.poly.model.User;
import com.poly.service.OrderService;
import com.poly.service.ProductService;
import com.poly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/dashboard")
@CrossOrigin(origins = "*")
public class AdminDashBoardRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/users-today")
    public List<User> getUsersCreatedToday() {
        return userService.getUsersCreatedToday();
    }

    @GetMapping("/products-today")
    public List<Product> getProductsCreatedToday() {
        return productService.getProductsCreatedToday();
    }

    @GetMapping("/today")
    public Map<String, Object> getTodayOrdersAndRevenue() {
        List<Order> orders = orderService.getOrdersTodayWithStateDelivered();
        Double totalRevenue = orderService.getTotalRevenueTodayWithStateDelivered();

        Map<String, Object> response = new HashMap<>();
        response.put("orders", orders);
        response.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);

        return response;
    }
    
    
    @GetMapping("/weekly-sales")
    public List<Map<String, Object>> getWeeklySales() {
        List<Object[]> weeklySalesData = orderService.getWeeklySales();

        // Chuyển đổi kết quả trả về thành danh sách map để dễ sử dụng trong frontend
        List<Map<String, Object>> response = weeklySalesData.stream().map(data -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("day", data[0]); // Tên ngày (Thứ 2, Thứ 3,...)
            entry.put("total", data[1]); // Tổng doanh số
            return entry;
        }).toList();

        return response;
    }
}
