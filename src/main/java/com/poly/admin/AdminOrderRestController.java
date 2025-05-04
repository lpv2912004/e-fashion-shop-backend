	package com.poly.admin;
	
	import com.poly.dto.OrderDTO;
	import com.poly.dto.OrderDetailDTO;
	import com.poly.model.Order;
	import com.poly.model.OrderDetail;
import com.poly.model.OrderMoreVoucher;
import com.poly.model.State;
	import com.poly.model.VariantProduct;
	import com.poly.repository.OrderDetailRepository;
	import com.poly.repository.OrderRepository;
	import com.poly.service.EmailOrderService;
	import com.poly.service.EmailService;
	import com.poly.service.OrderService;
	import com.poly.service.VariantProductService;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.scheduling.annotation.Async;
	import org.springframework.web.bind.annotation.*;
	
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Map;
	import java.util.Optional;
	import java.util.stream.Collectors;
	
	@RestController
	@RequestMapping("admin/api/orders")
	@CrossOrigin(origins = "*") 
	public class AdminOrderRestController {
	
	    @Autowired
	    private OrderService orderService;
	    @Autowired
	    private EmailOrderService emailOrders;
	    @Autowired
	    private OrderRepository orderRepository;
	
	    @Autowired
	    private OrderDetailRepository orderDetailRepository;
	    @Autowired
	    private VariantProductService variantProductService;
	
	    @GetMapping
	    public ResponseEntity<List<OrderDTO>> getAllOrders() {
	        List<Order> orders = orderRepository.findAllAccept_order();
	        List<OrderDTO> orderDTOs = new ArrayList<>();
	
	   
	        for (Order order : orders) {
	            List<OrderDetail> orderDetails = orderDetailRepository.findbyOrderId(order.getId());
	
	
	            List<OrderDetailDTO> orderDetailDTOs = new ArrayList<>();
	            for (OrderDetail orderDetail : orderDetails) {
	                OrderDetailDTO detailDTO = new OrderDetailDTO();
	                detailDTO.setId(orderDetail.getId());
	                detailDTO.setProductName(orderDetail.getVariantProd().getProduct().getName_prod());
	                detailDTO.setVariantName(orderDetail.getName_color());
	                detailDTO.setVariantSize(orderDetail.getName_size());
	                detailDTO.setPrice(orderDetail.getPrice());
	                detailDTO.setQuantity(orderDetail.getQuantity());
	                detailDTO.setDiscount_FS(orderDetail.getDiscount_FS());
	//                detailDTO.setDiscount_voucher(orderDetail.getDiscount_voucher());
	                detailDTO.setName_FS(orderDetail.getName_FS());
	
	                // Add to list
	                orderDetailDTOs.add(detailDTO);
	            }
	
	            // Create OrderDTO
	            OrderDTO orderDTO = new OrderDTO();
	            orderDTO.setOrderId(order.getId());
	            orderDTO.setOrderDetails(orderDetailDTOs);
	            orderDTO.setDeliveryFee(order.getDelivery_fee());
	            orderDTO.setTotalCash(order.getTotal_cash());
	            orderDTO.setOrderStatus(order.getState().getName_status_order());

	            // Add orderDTO to list
	            orderDTOs.add(orderDTO);
	        }
	
	        return ResponseEntity.ok(orderDTOs);
	    }
	
	
	    @GetMapping("/{id}")
	    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer id) {
	        Optional<Order> orderOpt = orderService.findById(id);

	        if (orderOpt.isPresent()) {
	            Order order = orderOpt.get();
	            List<OrderDetail> orderDetails = orderDetailRepository.findbyOrderId(order.getId());

	            List<OrderDetailDTO> orderDetailDTOs = new ArrayList<>();
	            for (OrderDetail orderDetail : orderDetails) {
	                OrderDetailDTO detailDTO = new OrderDetailDTO();
	                detailDTO.setId(orderDetail.getId());
	                detailDTO.setProductName(orderDetail.getVariantProd().getProduct().getName_prod());
	                detailDTO.setVariantName(orderDetail.getName_color());
	                detailDTO.setVariantSize(orderDetail.getName_size());
	                detailDTO.setPrice(orderDetail.getPrice());
	                detailDTO.setQuantity(orderDetail.getQuantity());
	                detailDTO.setDiscount_FS(orderDetail.getDiscount_FS());
	                // Không lấy discount_voucher từ OrderDetail, mà từ OrderMoreVoucher
	                detailDTO.setName_FS(orderDetail.getName_FS());

	                orderDetailDTOs.add(detailDTO);
	            }

	            // Lấy discount_voucher từ OrderMoreVoucher
	            Double totalDiscountVoucher = 0.0;
	            String nameVoucher = "";  // Khai báo để lưu tên voucher (mã voucher)
	            for (OrderMoreVoucher orderMoreVoucher : order.getOrderMoreVouchers()) {
	                totalDiscountVoucher += orderMoreVoucher.getDiscount_voucher(); // Cộng dồn discount voucher
	                
	                // Lấy mã voucher từ đối tượng Voucher liên kết
	                if (orderMoreVoucher.getVoucher() != null) {
	                    nameVoucher = orderMoreVoucher.getVoucher().getCode();  // Lấy mã voucher
	                }
	            }

	            OrderDTO orderDTO = new OrderDTO();
	            orderDTO.setOrderId(order.getId());
	            orderDTO.setOrderDetails(orderDetailDTOs);
	            orderDTO.setDeliveryFee(order.getDelivery_fee());
	            orderDTO.setTotalCash(order.getTotal_cash());
	            orderDTO.setOrderStatus(order.getState().getName_status_order());
	            orderDTO.setUserFullName(order.getUser().getFullName());
	            orderDTO.setUserEmail(order.getUser().getEmail());
	            orderDTO.setUserPhone(order.getUser().getPhone());
	            orderDTO.setShippingAddress(order.getAddress().getFull_address());
	            orderDTO.setCreatedDate(order.getCreated_date());
	            orderDTO.setPaymentMethod(order.getMethodPayment().getName_method());
	            orderDTO.setDiscount_voucher(totalDiscountVoucher);  // Gán giá trị discount_voucher cho DTO
	            orderDTO.setName_voucher(nameVoucher); // Gán giá trị discount_voucher cho DTO

	            return ResponseEntity.ok(orderDTO);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }

	
	
	    @PutMapping("/{id}")
	    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody Order orderDetails) {
	        Optional<Order> order = orderService.findById(id);
	
	        if (order.isPresent()) {
	            Order updatedOrder = order.get();
	            updatedOrder.setState(orderDetails.getState());
	            return ResponseEntity.ok(orderService.save(updatedOrder));
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
	    
//	    @PostMapping("/{id}/cancel")
//	    public ResponseEntity<String> cancelOrder(@PathVariable Integer id, @RequestBody Map<String, String> request) {
//	        Optional<Order> orderOpt = orderService.findById(id);
//	        if (!orderOpt.isPresent()) {
//	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đơn hàng");
//	        }
//	
//	        Order order = orderOpt.get();
//	        String reason = request.get("reason");
//	
//	        // Gắn lý do hủy vào trường "note" của đơn hàng
//	        order.setNote(reason);
//	
//	        // Cập nhật trạng thái đơn hàng thành "Đã hủy"
//	        State cancelState = new State();
//	        cancelState.setId(6); // Điều chỉnh ID của trạng thái "Đã hủy" nếu cần
//	        order.setState(cancelState);
//	
//	        // Lưu thay đổi đơn hàng
//	        orderService.save(order);
//	
//	        // Trả lại số lượng sản phẩm cho các biến thể trong OrderDetail
//	        for (OrderDetail detail : order.getOrderDetails()) {
//	            VariantProduct variant = detail.getVariantProd();
//	            int quantityToReturn = detail.getQuantity();
//	            variant.setQuantity(variant.getQuantity() + quantityToReturn); // Cộng số lượng đã hủy vào tồn kho
//	            variantProductService.save(variant); // Lưu lại biến thể sau khi cập nhật số lượng
//	        }
//	
//	        // Tách việc gửi email ra, thực hiện không đồng bộ sau khi trả về phản hồi
//	        sendCancellationEmailAsync(order, reason);
//	
//	        return ResponseEntity.ok("Đã hủy đơn hàng, hoàn lại số lượng sản phẩm. Email sẽ được gửi sau.");
//	    }
//	
//	    // Phương thức gửi email không đồng bộ
//	    @Async
//	    public void sendCancellationEmailAsync(Order order, String reason) {
//	        // Thu thập thông tin chi tiết đơn hàng để gửi email
//	        String customerName = order.getUser().getFullName();
//	        String shippingAddress = order.getAddress().getFull_address();
//	        double deliveryFee = order.getDelivery_fee();
//	        double totalCash = order.getTotal_cash();
//	
//	        // Lấy danh sách sản phẩm từ OrderDetail
//	        List<String> productDetails = order.getOrderDetails().stream()
//	                .map(detail -> detail.getVariantProd().getProduct().getName_prod() 
//	                     + " - Màu sắc: " + detail.getName_color() 
//	                     + ", Kích thước: " + detail.getName_size() 
//	                     + ", Số lượng: " + detail.getQuantity() 
//	                     + ", Giá: " + detail.getPrice())
//	                .collect(Collectors.toList());
//	
//	        // Gọi dịch vụ gửi email
//	        emailOrders.sendOrderCancellationEmail(
//	            order.getUser().getEmail(),
//	            order.getId().toString(),
//	            reason,
//	            customerName,
//	            shippingAddress,
//	            deliveryFee,
//	            totalCash,
//	            productDetails
//	        );
//	    }
//	
	
	
	
	    @PostMapping("/{id}/cancel")
	    public ResponseEntity<String> cancelOrder(@PathVariable Integer id, @RequestBody Map<String, String> request) {
	        Optional<Order> orderOpt = orderService.findById(id);
	        if (!orderOpt.isPresent()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đơn hàng");
	        }

	        Order order = orderOpt.get();
	        String reason = request.get("reason");

	        // Gọi phương thức xử lý hủy đơn hàng
	        handleOrderCancellation(order, reason);

	        // Trả về ngay lập tức trạng thái thành công mà không chờ gửi email
	        return ResponseEntity.ok("Đã hủy đơn hàng, hoàn lại số lượng sản phẩm. Email sẽ được gửi ngầm.");
	    }

	    /**
	     * Xử lý logic hủy đơn hàng.
	     */
	    private void handleOrderCancellation(Order order, String reason) {
	        // Gắn lý do hủy vào trường "note" của đơn hàng
	        order.setNote(reason);

	        // Cập nhật trạng thái đơn hàng thành "Đã hủy"
	        State cancelState = new State();
	        cancelState.setId(6); // Điều chỉnh ID của trạng thái "Đã hủy" nếu cần
	        order.setState(cancelState);

	        // Lưu thay đổi đơn hàng
	        orderService.save(order);

	        // Gửi email không đồng bộ
	        sendCancellationEmailAsync(order, reason);

	        // Trả lại số lượng sản phẩm cho các biến thể trong OrderDetail
	        for (OrderDetail detail : order.getOrderDetails()) {
	            VariantProduct variant = detail.getVariantProd();
	            int quantityToReturn = detail.getQuantity();
	            variant.setQuantity(variant.getQuantity() + quantityToReturn); // Cộng số lượng đã hủy vào tồn kho
	            variantProductService.save(variant); // Lưu lại biến thể sau khi cập nhật số lượng
	        }
	    }

	    /**
	     * Phương thức gửi email không đồng bộ.
	     */
	    @Async
	    public void sendCancellationEmailAsync(Order order, String reason) {
	        try {
	            // Thu thập thông tin chi tiết đơn hàng để gửi email
	            String customerName = order.getUser().getFullName();
	            String shippingAddress = order.getAddress().getFull_address();
	            double deliveryFee = order.getDelivery_fee();
	            double totalCash = order.getTotal_cash();

	            // Lấy danh sách sản phẩm từ OrderDetail
	            List<String> productDetails = order.getOrderDetails().stream()
	                    .map(detail -> detail.getVariantProd().getProduct().getName_prod() 
	                         + " - Màu sắc: " + detail.getName_color() 
	                         + ", Kích thước: " + detail.getName_size() 
	                         + ", Số lượng: " + detail.getQuantity() 
	                         + ", Giá: " + detail.getPrice())
	                    .collect(Collectors.toList());

	            // Gọi dịch vụ gửi email
	            emailOrders.sendOrderCancellationEmail(
	                order.getUser().getEmail(),
	                order.getId().toString(),
	                reason,
	                customerName,
	                shippingAddress,
	                deliveryFee,
	                totalCash,
	                productDetails
	            );
	        } catch (Exception e) {
	            // Xử lý lỗi gửi email nếu cần
	            System.err.println("Lỗi khi gửi email hủy đơn hàng: " + e.getMessage());
	        }
	    }


	
	
	}
