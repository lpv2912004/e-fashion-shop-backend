package com.poly.service;

import com.poly.dto.CheckoutElement;
import com.poly.dto.Item;
import com.poly.model.Address;
import com.poly.model.MethodPayment;
import com.poly.model.Order;
import com.poly.model.OrderDetail;
import com.poly.model.State;
import com.poly.model.User;
import com.poly.model.VariantProduct;
import com.poly.repository.AddressRepository;
import com.poly.repository.CartRepository;
import com.poly.repository.MethodPaymentRepository;
import com.poly.repository.OrderDetailRepository;
import com.poly.repository.OrderRepository;
import com.poly.repository.StateRepository;
import com.poly.repository.UserRepository;
import com.poly.repository.VariantProductRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	@Autowired
	private OrderRepository orderRepository;

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public Optional<Order> findById(Integer id) {
		return orderRepository.findById(id);
	}

	public Order save(Order order) {
		return orderRepository.save(order);
	}

	public void deleteById(Integer id) {
		orderRepository.deleteById(id);
	}

	public List<OrderDetail> findByOrderId(Integer orderId) {
		return orderDetailRepository.findByOrderId(orderId);
	}
	
	public List<Object[]> getWeeklySales() {
		return orderRepository.findWeeklySales();
	}

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private MethodPaymentRepository methodPaymentRepository;

	@Autowired
	private VariantProductRepository variantProductRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private StateRepository stateRepository;

	@Transactional
	public void createOrder(CheckoutElement checkoutElement) {
		try {
			
			Integer userId = checkoutElement.getItems().get(0).getUser().getId();
			Integer methodPaymentId = checkoutElement.getPaymentMethod();
			Integer stateId = checkoutElement.getState();
			
			// Kiểm tra xem tất cả các ID không được null
//			if (checkoutElement.getAddress() == null || checkoutElement.getPaymentMethod() == null) {
//				throw new IllegalArgumentException("Address and payment method cannot be null");
//			}


			System.out.println("Address: " + checkoutElement.getAddress());
			System.out.println("Payment Method: " + checkoutElement.getPaymentMethod());
			
			if (userId == null || methodPaymentId == null) {
				throw new IllegalArgumentException("User ID and payment method ID cannot be null");
			}

			// Tìm kiếm các thực thể liên quan từ database
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));
			MethodPayment methodPayment = methodPaymentRepository.findById(methodPaymentId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid payment method ID"));
			State state = stateRepository.findById(stateId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid state ID"));

			// Lấy địa chỉ đã chọn
			Integer addressId = checkoutElement.getAddress();
			Address address = addressRepository.findById(addressId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid address ID"));

			// Tạo đơn hàng mới
			Order order = new Order();
			order.setUser(user);
			order.setAddress(address);
			order.setMethodPayment(methodPayment);
			order.setTotal_cash(checkoutElement.getTotalPrice().floatValue());
			order.setDelivery_fee(checkoutElement.getDeliveryFee().floatValue());
			order.setState(state);

			// Lưu đơn hàng vào cơ sở dữ liệu
			orderRepository.save(order);

			// Tạo các chi tiết đơn hàng và cập nhật số lượng biến thể sản phẩm
			for (Item item : checkoutElement.getItems()) {
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setOrder(order);
				orderDetail.setVariantProd(item.getVariantProduct());
				orderDetail.setQuantity(item.getQuantity());
				orderDetail.setPrice(item.getVariantProduct().getPrice() * item.getQuantity());

				// Lưu chi tiết đơn hàng vào cơ sở dữ liệu
				orderDetailRepository.save(orderDetail);

				// Cập nhật số lượng biến thể sản phẩm
				VariantProduct variantProduct = item.getVariantProduct();
				int newQuantity = variantProduct.getQuantity() - item.getQuantity();
				if (newQuantity < 0) {
					throw new IllegalArgumentException("Insufficient stock for product: " + variantProduct.getId());
				}
				variantProduct.setQuantity(newQuantity);
				variantProductRepository.save(variantProduct);

				// Xóa mục đã đặt từ giỏ hàng
				cartRepository.deleteByUserIdAndVariantProdId(userId, variantProduct.getId());
			}
		} catch (Exception e) {
			// Ghi lại lỗi nếu có vấn đề xảy ra trong quá trình tạo đơn hàng
			e.printStackTrace();
			throw new RuntimeException("Error creating order: " + e.getMessage());
		}
	}
	
	
	public List<Order> getOrdersTodayWithStateDelivered() {
		return orderRepository.findOrdersTodayWithStateDelivered();
	}

	public Double getTotalRevenueTodayWithStateDelivered() {
		return orderRepository.findTotalRevenueTodayWithStateDelivered();
	}

}
