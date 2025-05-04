package com.poly.user;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.CheckoutElement;
import com.poly.dto.Item;
import com.poly.dto.NotificationRequest;
import com.poly.model.ActivitySale;
import com.poly.model.Address;
import com.poly.model.FlashSale;
import com.poly.model.Mail;
import com.poly.model.MethodPayment;
import com.poly.model.Order;
import com.poly.model.OrderDetail;
import com.poly.model.OrderMoreVoucher;
import com.poly.model.State;
import com.poly.model.Transaction;
import com.poly.model.User;
import com.poly.model.VariantProduct;
import com.poly.model.Voucher;
import com.poly.repository.ActivityFlashSaleRepository;
import com.poly.repository.AddressRepository;
import com.poly.repository.CartRepository;
import com.poly.repository.MethodPaymentRepository;
import com.poly.repository.NotificationRepository;
import com.poly.repository.OrderDetailRepository;
import com.poly.repository.OrderMoreVoucherRepository;
import com.poly.repository.OrderRepository;
import com.poly.repository.StateRepository;
import com.poly.repository.TransactionRepository;
import com.poly.repository.UserRepository;
import com.poly.repository.VariantProductRepository;
import com.poly.repository.VoucherRepository;
import com.poly.service.EmailServiceWithWait;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("user/api/order")
public class UserOrderRestController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private MethodPaymentRepository methodPaymentRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	VariantProductRepository variantProductRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	ActivityFlashSaleRepository activityFlashSaleRepository;

	@Autowired
	VoucherRepository voucherRepository;
	
	@Autowired
	OrderMoreVoucherRepository orderMoreVoucherRepository;
	
	@Autowired
	NotificationRepository notificationRepository;
	
	@Autowired
	EmailServiceWithWait emailServ;

	@PostMapping("create")
	@Transactional
	public ResponseEntity<?> createOrder(@RequestBody CheckoutElement checkoutElement) {
		try {
			System.out.println(checkoutElement);
			// Kiểm tra xem tất cả các ID không được null
			if (checkoutElement.getAddress() == null || checkoutElement.getPaymentMethod() == null) {
				return ResponseEntity.badRequest().body("Address and payment method cannot be null");
			}

			Integer userId = checkoutElement.getItems().get(0).getUser().getId();
			Integer methodPaymentId = checkoutElement.getPaymentMethod();
			Integer stateInteger = checkoutElement.getState();
			
			if (userId == null || methodPaymentId == null) {
				return ResponseEntity.badRequest().body("Không được có giá trị null");
			}

			// Tìm kiếm các thực thể liên quan từ database
			User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Sai IdUser"));
			MethodPayment methodPayment = methodPaymentRepository.findById(methodPaymentId)
					.orElseThrow(() -> new IllegalArgumentException("Sai id phương thức thanh toán"));
			State state = stateRepository.findById(stateInteger)
					.orElseThrow(() -> new IllegalArgumentException("Sai id trạng thái"));
			

			// Tạo và lưu địa chỉ mới
			// lấy địa chỉ đầu tiên
			Integer id_address = checkoutElement.getAddress();
			Address address = addressRepository.findById(id_address).get();

			// Tạo đơn hàng
	        Order order = new Order();
	        order.setUser(user);
	        order.setAddress(address);
	        order.setMethodPayment(methodPayment);
	        order.setTotal_cash(checkoutElement.getTotalPrice().floatValue());
	        order.setDelivery_fee(checkoutElement.getDeliveryFee().floatValue());
	        order.setState(state);
	        order.setAccept_order(Boolean.TRUE);
	        order.setNote(null);

	        orderRepository.save(order);

	        // Lặp qua danh sách voucher và xử lý
	        for (Integer voucherId : checkoutElement.getId_vouchers()) {
	            Voucher voucher = voucherRepository.findById(voucherId.longValue())
	                    .orElseThrow(() -> new IllegalArgumentException("Sai id voucher"));

	            // Giảm số lượng voucher (nếu cần)
	            voucher.setQuantity(voucher.getQuantity() - 1);
	            voucherRepository.save(voucher);

	            // Tạo đối tượng OrderMoreVoucher
	            OrderMoreVoucher orderMoreVoucher = new OrderMoreVoucher();
	            orderMoreVoucher.setOrder(order);
	            orderMoreVoucher.setVoucher(voucher);
	            orderMoreVoucher.setDiscount_voucher(voucher.getDiscount().doubleValue());

	            // Lưu OrderMoreVoucher vào database
	            orderMoreVoucherRepository.save(orderMoreVoucher);
	        }

	        // Lặp qua các Item để tạo chi tiết đơn hàng và cập nhật số lượng biến thể
	        for (Item item : checkoutElement.getItems()) {
	            OrderDetail orderDetail = new OrderDetail();

	            // Tính giá và thêm các thông tin liên quan đến ActivitySale (nếu có)
	            Optional<ActivitySale> activitySaleOpt = activityFlashSaleRepository
	                    .findActiveDiscountByVariantId(item.getVariantProduct().getId());

	            orderDetail.setOrder(order);
	            orderDetail.setVariantProd(item.getVariantProduct());
	            orderDetail.setQuantity(item.getQuantity());
	            orderDetail.setFull_address(item.getFullAddress());
	            orderDetail.setName_color(item.getNameColor());
	            orderDetail.setName_size(item.getNameSize());
	         

	            if (activitySaleOpt.isPresent()) {
	                ActivitySale activitySale = activitySaleOpt.get();
	                FlashSale flashSale = activitySale.getFlashSale();
	                double discountedPrice = item.getVariantProduct().getPrice()
	                        - (item.getVariantProduct().getPrice() * activitySale.getDiscount_percent() / 100);
	                orderDetail.setPrice(discountedPrice);
	                orderDetail.setDiscount_FS(activitySale.getDiscount_percent());
	                orderDetail.setName_FS(flashSale.getName_FS());
	            } else {
	                orderDetail.setPrice(item.getVariantProduct().getPrice());
	                orderDetail.setDiscount_FS(0);
	                orderDetail.setName_FS(null);
	            }

	            orderDetailRepository.save(orderDetail);

	            // Cập nhật số lượng biến thể
	            VariantProduct variantProduct = item.getVariantProduct();
	            int newQuantity = variantProduct.getQuantity() - item.getQuantity();
	            if (newQuantity < 0) {
	                return ResponseEntity.badRequest().body("Không đủ dữ liệu: " + variantProduct.getId());
	            }
	            variantProduct.setQuantity(newQuantity);
	            variantProductRepository.save(variantProduct);

	            // Xóa mục đã đặt từ giỏ hàng
	            cartRepository.deleteByUserIdAndVariantProdId(userId, variantProduct.getId());
	        }

	        return ResponseEntity.ok(order);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi tạo đơn hàng: " + e.getMessage());
		}
	}

	@PostMapping("createbyvnpay")
	@Transactional
	public ResponseEntity<?> createOrderByVNPay(@RequestBody CheckoutElement checkoutElement) {
		try {
			System.out.println(checkoutElement);
			System.out.println("VNPay Transaction Status: " + checkoutElement.getVnp_TransactionStatus());
			System.out.println("VNPay Transaction No: " + checkoutElement.getVnp_TransactionNo());

			// Kiểm tra xem tất cả các ID không được null
			if (checkoutElement.getAddress() == null || checkoutElement.getPaymentMethod() == null) {
				return ResponseEntity.badRequest().body("Address and payment method cannot be null");
			}

			// Nếu trạng thái giao dịch thành công (00)
			if (checkoutElement.getVnp_TransactionStatus().equalsIgnoreCase("00")) {
				Integer userId = checkoutElement.getItems().get(0).getUser().getId();
				Integer methodPaymentId = checkoutElement.getPaymentMethod();
				Integer stateInteger = checkoutElement.getState();
//				Integer id_voucher = checkoutElement.getItems().get(0).getId_voucher();

				if (userId == null || methodPaymentId == null) {
					return ResponseEntity.badRequest().body("Không được có giá trị null");
				}

				// Tìm kiếm các thực thể liên quan từ database
				User user = userRepository.findById(userId)
						.orElseThrow(() -> new IllegalArgumentException("Sai IdUser"));
				MethodPayment methodPayment = methodPaymentRepository.findById(methodPaymentId)
						.orElseThrow(() -> new IllegalArgumentException("Sai id phương thức thanh toán"));
				State state = stateRepository.findById(stateInteger)
						.orElseThrow(() -> new IllegalArgumentException("Sai id trạng thái"));

//				Voucher voucher = null;
//				
//				if (id_voucher != null) {
//					voucher = voucherRepository.findById(Long.valueOf(id_voucher.longValue()))
//							.orElseThrow(() -> new IllegalArgumentException("Sai id voucher"));
//				}

				// Lấy địa chỉ từ database
				Integer id_address = checkoutElement.getAddress();
				Address address = addressRepository.findById(id_address).get();

				// Tạo đơn hàng
				// Tạo đơn hàng
		        Order order = new Order();
		        order.setUser(user);
		        order.setAddress(address);
		        order.setMethodPayment(methodPayment);
		        order.setTotal_cash(checkoutElement.getTotalPrice().floatValue());
		        order.setDelivery_fee(checkoutElement.getDeliveryFee().floatValue());
		        order.setState(state);
		        order.setAccept_order(Boolean.TRUE);
		        order.setNote(null);

		        orderRepository.save(order);

		        // Lặp qua danh sách voucher và xử lý
		        for (Integer voucherId : checkoutElement.getId_vouchers()) {
		            Voucher voucher = voucherRepository.findById(voucherId.longValue())
		                    .orElseThrow(() -> new IllegalArgumentException("Sai id voucher"));

		            // Giảm số lượng voucher (nếu cần)
		            voucher.setQuantity(voucher.getQuantity() - 1);
		            voucherRepository.save(voucher);

		            // Tạo đối tượng OrderMoreVoucher
		            OrderMoreVoucher orderMoreVoucher = new OrderMoreVoucher();
		            orderMoreVoucher.setOrder(order);
		            orderMoreVoucher.setVoucher(voucher);
		            orderMoreVoucher.setDiscount_voucher(voucher.getDiscount().doubleValue());

		            // Lưu OrderMoreVoucher vào database
		            orderMoreVoucherRepository.save(orderMoreVoucher);
		        }

		        // Lặp qua các Item để tạo chi tiết đơn hàng và cập nhật số lượng biến thể
		        for (Item item : checkoutElement.getItems()) {
		            OrderDetail orderDetail = new OrderDetail();

		            // Tính giá và thêm các thông tin liên quan đến ActivitySale (nếu có)
		            Optional<ActivitySale> activitySaleOpt = activityFlashSaleRepository
		                    .findActiveDiscountByVariantId(item.getVariantProduct().getId());

		            orderDetail.setOrder(order);
		            orderDetail.setVariantProd(item.getVariantProduct());
		            orderDetail.setQuantity(item.getQuantity());
		            orderDetail.setFull_address(item.getFullAddress());
		            orderDetail.setName_color(item.getNameColor());
		            orderDetail.setName_size(item.getNameSize());

		            if (activitySaleOpt.isPresent()) {
		                ActivitySale activitySale = activitySaleOpt.get();
		                FlashSale flashSale = activitySale.getFlashSale();
		                double discountedPrice = item.getVariantProduct().getPrice()
		                        - (item.getVariantProduct().getPrice() * activitySale.getDiscount_percent() / 100);
		                orderDetail.setPrice(discountedPrice);
		                orderDetail.setDiscount_FS(activitySale.getDiscount_percent());
		                orderDetail.setName_FS(flashSale.getName_FS());
		            } else {
		                orderDetail.setPrice(item.getVariantProduct().getPrice());
		                orderDetail.setDiscount_FS(0);
		                orderDetail.setName_FS(null);
		            }

		            orderDetailRepository.save(orderDetail);

		            // Cập nhật số lượng biến thể
		            VariantProduct variantProduct = item.getVariantProduct();
		            int newQuantity = variantProduct.getQuantity() - item.getQuantity();
		            if (newQuantity < 0) {
		                return ResponseEntity.badRequest().body("Không đủ dữ liệu: " + variantProduct.getId());
		            }
		            variantProduct.setQuantity(newQuantity);
		            variantProductRepository.save(variantProduct);

		            // Xóa mục đã đặt từ giỏ hàng
		            cartRepository.deleteByUserIdAndVariantProdId(userId, variantProduct.getId());
		        }
		        
		        

		        return ResponseEntity.ok(order);
			}

			// Nếu trạng thái giao dịch không thành công
			else {
				Integer userId = checkoutElement.getItems().get(0).getUser().getId();
				Integer methodPaymentId = checkoutElement.getPaymentMethod();
				Integer stateInteger = checkoutElement.getState();

				if (userId == null || methodPaymentId == null) {
					return ResponseEntity.badRequest().body("Không được có giá trị null");
				}

				// Tìm kiếm các thực thể liên quan từ database
				User user = userRepository.findById(userId)
						.orElseThrow(() -> new IllegalArgumentException("Sai IdUser"));
				MethodPayment methodPayment = methodPaymentRepository.findById(methodPaymentId)
						.orElseThrow(() -> new IllegalArgumentException("Sai id phương thức thanh toán"));
				State state = stateRepository.findById(stateInteger)
						.orElseThrow(() -> new IllegalArgumentException("Sai id trạng thái"));

				// Lấy địa chỉ từ database
				Integer id_address = checkoutElement.getAddress();
				Address address = addressRepository.findById(id_address).get();

				// Tạo đơn hàng
				Order order = new Order();
				order.setUser(user);
				order.setAddress(address);
				order.setMethodPayment(methodPayment);
				order.setTotal_cash(checkoutElement.getTotalPrice().floatValue());
				order.setDelivery_fee(checkoutElement.getDeliveryFee().floatValue());
				order.setState(state);
				order.setAccept_order(Boolean.FALSE);
				order.setNote("Tạo đơn hàng thất bại!");
//				order.setVoucher(null);
				orderRepository.save(order);

				// Tạo giao dịch thất bại
				Transaction transaction = new Transaction();
				transaction.setOrder(order);
				transaction.setTotal(checkoutElement.getTotalPrice().floatValue());
				transaction.setTransactionCode(checkoutElement.getVnp_TransactionNo());
				transaction.setStatus("Failed");
				transactionRepository.save(transaction);

				// Trả về đơn hàng trong trường hợp giao dịch thất bại
				return ResponseEntity.ok(order);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi tạo đơn hàng: " + e.getMessage());
		}
	}

	@GetMapping("/all/{id}")
	public ResponseEntity<List<Order>> getAllOrderByUser(@PathVariable("id") Integer id_user) {
		try {
			List<Order> list = orderRepository.findALlOrderByUser(id_user);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@GetMapping("/{orderId}")
	public ResponseEntity<?> getOrderById(@PathVariable Integer orderId) {
		try {
			Order order = orderRepository.findById(orderId)
					.orElseThrow(() -> new IllegalArgumentException("Order not found"));
			return ResponseEntity.ok(order);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error fetching order: " + e.getMessage());
		}
	}

	
	@PostMapping("/verifyOrder/{id}")
	public ResponseEntity<?> acceptOrder(@PathVariable Integer id){
		Optional<Order> optional = orderRepository.findById(id);
		try {
			if(optional.isPresent()) {
				Order order = optional.get();
				State verifyOrder = stateRepository.findById(9).get();
				order.setId(id);
				order.setState(verifyOrder);
				orderRepository.save(order);
			}
			 return ResponseEntity.ok("Đã xác nhận đơn hàng");
		} catch (Exception e) {
			e.printStackTrace();
			 return ResponseEntity.badRequest().body("Cập nhật đơn hàng thất bại");
		}
	}
	
	
	@PutMapping("/cancel/{orderId}")
	public ResponseEntity<String> cancelOrder(@PathVariable Integer orderId, @RequestBody Map<String, String> body) {
	    Optional<Order> optionalOrder = orderRepository.findById(orderId);

	    if (optionalOrder.isPresent()) {
	        Order order = optionalOrder.get();

	        // Nhận lý do hủy từ body của request
	        String selectedReason = body.get("reason");
	        if (selectedReason == null || selectedReason.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Reason for cancellation is required.");
	        }

	        // Cập nhật trạng thái đơn hàng thành 'canceled'
	        State canceledState = new State();
	        canceledState.setId(6); // Giả sử ID 6 là 'canceled'
	        order.setState(canceledState);

	        // Lưu lý do hủy vào trường note của đơn hàng
	        order.setNote(selectedReason);
	        
	        orderRepository.save(order);

	        // Khôi phục số lượng sản phẩm
	        List<OrderDetail> orderDetails = order.getOrderDetails();
	        
	        for (OrderDetail detail : orderDetails) {
	            VariantProduct variant = detail.getVariantProd();
	            variant.setQuantity(variant.getQuantity() + detail.getQuantity());
	            variantProductRepository.save(variant);
	        }

	        // Tìm và xóa các OrderMoreVoucher liên quan
	        List<OrderMoreVoucher> orderMoreVouchers = orderMoreVoucherRepository.findByOrder(order);
	        
	        for (OrderMoreVoucher orderMoreVoucher : orderMoreVouchers) {
	            // Khôi phục số lượng voucher nếu cần
	            Voucher voucher = orderMoreVoucher.getVoucher();
	            if (voucher != null) {
	                voucher.setQuantity(voucher.getQuantity() + 1);
	                voucherRepository.save(voucher);
	            }

	            // Xóa bản ghi OrderMoreVoucher
	            orderMoreVoucherRepository.delete(orderMoreVoucher);
	        }
	        
	        
	        
	        String htmlContent = "<html>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 10px;'>"
                    + "<div style='text-align: center; padding: 10px;'>"
                    + "<img src='https://simicart.com/wp-content/uploads/eCommerce-logo.jpg' alt='Logo' style='width: 80px; height: auto;' />"
                    + "</div>"

                    + "<div style='text-align: center; background-color: #f7f7f7; padding: 20px; border-radius: 5px;'>"
                    + "<p style='color: #555;'>Đơn hàng #" + orderId + " Đã hủy vì lí do: " + selectedReason + "</p>"
                    + "<h1 style='font-size: 20px; color: #000;'>" + "Nếu đơn hàng của bạn thanh toán Trực tuyến thì vui lòng liên hệ với Hotline: 09001522 để được hỗ trợ hoàn tiền" + "</h1>"
                    + "</div>"
                    + "<div style='text-align: center; margin-top: 20px;'>"
               
                    + "</div>"

                    + "<footer style='text-align: center; margin-top: 20px;'>"
                    + "<p style='color: #888;'>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi</p>"
                    + "<div style='margin-top: 10px;'>"
                    + "<a href='#'><img src='https://cdn-icons-png.flaticon.com/512/59/59439.png' alt='Facebook' style='width: 30px; margin-right: 5px;'/></a>"
                    + "<a href='#'><img src='https://i.pinimg.com/originals/96/71/ea/9671ea3716d6452e3e8c77e13f3e113c.jpg' alt='Instagram' style='width: 30px; margin-right: 5px;'/></a>"
                    + "<a href='#'><img src='https://static.vecteezy.com/system/resources/previews/018/910/797/original/youtube-logo-youtube-icon-youtube-symbol-free-free-vector.jpg' alt='YouTube' style='width: 50px; margin-right: 5px;'/></a>"
                    + "</div>"
                    + "</footer>"

                    + "</div>"
                    + "</body>"
                    + "</html>";
	        
	        String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(order.getUpdated_date());
	        emailServ.push(new Mail("phatnguyen2k31901@gmail.com",
	        		order.getUser().getEmail(),
	        		order.getUser().getFullName() + " ơi, Bạn đã hủy đơn hàng #" + orderId + " lúc: " + formattedDate, 
	        		htmlContent));
	       

	        System.out.println("Đã gửi đến Email " + order.getUser().getEmail());
	        return ResponseEntity.ok("Order canceled successfully.");
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order not found.");
	    }
	}

	
	
	// Hàm gửi thông báo đến admin
//	private void sendNotificationToAdmin(Order order) {
//	    try {
//	        // Lấy token của admin từ database
//	        com.poly.model.Notification notificationReq = notificationRepository.findById(1).get();  // Giả sử findLatestToken() trả về token gần nhất
//	        String adminToken = notificationReq.getName_notifi();
//	        Notification notification = Notification.builder()
//	                .setTitle("Đơn hàng mới")
//	                .setBody("Bạn có đơn hàng mới với ID: " + order.getId())
//	                .build();
//
//	        Message message = Message.builder()
//	                .setToken(adminToken)
//	                .setNotification(notification)
//	                .build();
//
//	        FirebaseMessaging.getInstance().send(message);
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}

}
