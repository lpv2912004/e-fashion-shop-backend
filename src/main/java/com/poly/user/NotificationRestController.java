package com.poly.user;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.poly.dto.NotificationRequest;
import com.poly.model.Order;
import com.poly.model.Role;
import com.poly.model.User;
import com.poly.repository.NotificationRepository;
import com.poly.repository.OrderRepository;
import com.poly.repository.RoleRepository;
import com.poly.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user/api/notify")
public class NotificationRestController {

	
//	@Autowired
//	private FirebaseMessaging firebaseMessaging;
	
	@Autowired
	@Qualifier("firebaseMessaging")
	private FirebaseMessaging firebaseMessaging;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	NotificationRepository notificationRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	OrderRepository orderRepository;

	@PostMapping("sendNotification")
	public ResponseEntity<?> sendNotification(@RequestBody NotificationRequest notificationRequest) {
		Message message = Message
				.builder().setToken(notificationRequest.getToken()).setNotification(Notification.builder()
						.setTitle(notificationRequest.getTitle()).setBody(notificationRequest.getBody()).build())
				.build();

		try {
		    String response = firebaseMessaging.send(message);
		    return ResponseEntity.ok("Successfully sent message: " + response);
		} catch (FirebaseMessagingException e) {
		    System.err.println("Error sending message: " + e.getMessage());  // Log error message
		    e.printStackTrace();  // Print full stack trace for debugging
		    return ResponseEntity.status(500).body("Error sending message: " + e.getMessage());
		}
	}

	@PostMapping("checkTokenDevice")
	public ResponseEntity<?> postMethodName(@RequestBody Map<String, Object> requestData) {
		Integer id_user = (Integer) requestData.get("id");
		String tokenDevice = (String) requestData.get("tokenDevice");
		User user = userRepository.findById(id_user).get();
		user.setId(id_user);
		user.setToken_device(tokenDevice);
		userRepository.save(user);
		return ResponseEntity.ok("đã cập nhật lại token device của user");
	}
	
	@PostMapping("createNotify")
	public ResponseEntity<?> postNotify(@RequestBody Map<String, Object> requestData) {
		Integer id_user = (Integer) requestData.get("idUser");
		String describe = (String) requestData.get("describe");
		User user = userRepository.findById(id_user).get();
		com.poly.model.Notification notify = new com.poly.model.Notification();
		notify.setUser(user);
		notify.setName_notifi(describe);
		notificationRepository.save(notify);
		return ResponseEntity.ok("Success Create Notify");
	}
	
	
	@PostMapping("createNotifyToAdmin")
	public ResponseEntity<?> postToAdmin(@RequestBody Map<String, Object> requestData) {
		String mess = (String) requestData.get("mess");
		Role role = roleRepository.findById(1).get();
		User user = userRepository.findByRole(role);
		com.poly.model.Notification notification = new com.poly.model.Notification();
		notification.setUser(user);
		notification.setName_notifi(mess);
		notification.setIs_reading(Boolean.FALSE);
		notificationRepository.save(notification);
		return ResponseEntity.ok("Success Create Notify");
	}
	
	
	
	@PostMapping("createNotifyToUser")
	public ResponseEntity<?> postToUser(@RequestBody Map<String, Object> requestData) {
		String mess = (String) requestData.get("mess");
		Integer idOrder = (Integer) requestData.get("idOrder");
		Order order = orderRepository.findById(idOrder).get();
		User user = order.getUser();
		com.poly.model.Notification notification = new com.poly.model.Notification();
		notification.setUser(user);
		notification.setName_notifi(mess);
		notification.setIs_reading(Boolean.FALSE);
		notificationRepository.save(notification);
		return ResponseEntity.ok("Success Create Notify");
	}
	
	@GetMapping("listNotificationAdmin")
	public ResponseEntity<?> getListNotification(){
		Role role = roleRepository.findById(1).get();
		User user = userRepository.findByRole(role);
		List<com.poly.model.Notification> list = notificationRepository.findNotification(user.getId());
		return ResponseEntity.ok(list);
	}
	
	
	@PostMapping("listNotificationUser")
	public ResponseEntity<?> getListNotificationOfUser(@RequestBody Map<String, Object> reqMap){
		Integer id = (Integer) reqMap.get("id");
		
		User user = userRepository.findById(id).get();
		List<com.poly.model.Notification> list = notificationRepository.findNotification(user.getId());
		return ResponseEntity.ok(list);
	}
	
	
	@PostMapping("updateReading")
	public ResponseEntity<?> updateListNotificationOfUser(@RequestBody Map<String, Object> reqMap){
		Integer id = (Integer) reqMap.get("idUser");
		
		User user = userRepository.findById(id).get();
		List<com.poly.model.Notification> list = notificationRepository.findNotification(user.getId());
		for (com.poly.model.Notification notification : list) {
			notification.setId(notification.getId());
			notification.setIs_reading(Boolean.TRUE);
			notificationRepository.save(notification);
		}
		
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/deleteNotification")
	public ResponseEntity<?> deleteNotification(@RequestBody Map<String, Object> reqMap) {
		List<com.poly.model.Notification> listNotifications = notificationRepository.findNotification((Integer) reqMap.get("idUser"));
		for (com.poly.model.Notification notification : listNotifications) {
			notificationRepository.delete(notification);
		}
		return ResponseEntity.ok("Đã xóa");
	}
	
	
}
