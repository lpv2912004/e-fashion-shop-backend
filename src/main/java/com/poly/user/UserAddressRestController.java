package com.poly.user;

import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.AddressDTO;
import com.poly.model.Address;
import com.poly.model.Order;
import com.poly.model.User;
import com.poly.repository.AddressRepository;
import com.poly.repository.OrderRepository;
import com.poly.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user/api/address")
public class UserAddressRestController {
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	OrderRepository orderRepository;
	@PostMapping("list/{id_user}")
	public ResponseEntity<List<Address>> getAllAddress(@PathVariable("id_user") Integer id_user) {
		User user = userRepository.findById(id_user).get();
		List<Address> list = addressRepository.findListByUserId(id_user);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("list/{id_user}")
	public ResponseEntity<List<Address>> getAllAddressByUser(@PathVariable("id_user") Integer id_user) {
		User user = userRepository.findById(id_user).get();
		List<Address> list = addressRepository.findListByUserId(id_user);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("byid/{id_address}")
	public ResponseEntity<Address> getAddress(@PathVariable("id_address") Integer id_address) {
		Address address = addressRepository.findById(id_address).get();
		return ResponseEntity.ok(address);
	}


//	@PostMapping("create")
//	public ResponseEntity<?> createAddressEntity(@RequestBody AddressDTO addressDTO) {
//
//		Address address = new Address();
//		User user = userRepository.findById(addressDTO.getId_user()).get();
//
//		address.setUser(user);
//		address.setFull_address(addressDTO.getFulladdress());
//		address.setId_district(addressDTO.getDistrictId());
//		address.setId_province(addressDTO.getProvinceId());
//		address.setId_ward(addressDTO.getWardId());
//		address.setIs_default(Boolean.FALSE);
//		addressRepository.save(address);
//
//		return ResponseEntity.ok(address);
//	}
	
//	@PostMapping("create")
//	public ResponseEntity<?> createAddressEntity(@RequestBody AddressDTO addressDTO) {
//
//	    Address address = new Address();
//	    User user = userRepository.findById(addressDTO.getId_user()).orElse(null);
//	    if (user == null) {
//	        return ResponseEntity.badRequest().body("User not found");
//	    }
//
//	    // Check if the user already has an address
//	    boolean hasAddress;
//	    
//	    List<Address> listAddresses = addressRepository.findByUserId(addressDTO.getId_user());
//	    for (Address address2 : listAddresses) {
//			if(address2.getIs_default()) {
//				hasAddress = false;
//				break;
//			}
//		}
//
//	    // Set the is_default based on whether the user already has an address or not
//	    address.setUser(user);
//	    address.setFull_address(addressDTO.getFulladdress());
//	    address.setId_district(addressDTO.getDistrictId());
//	    address.setId_province(addressDTO.getProvinceId());
//	    address.setId_ward(addressDTO.getWardId());
//	    address.setIs_default(hasAddress);  // Set true if no address exists, false otherwise
//	    address.setIs_deleted(Boolean.FALSE);
//	    addressRepository.save(address);
//
//	    return ResponseEntity.ok(address);
//	}
	
	@PostMapping("create")
	public ResponseEntity<?> createAddressEntity(@RequestBody AddressDTO addressDTO) {

	    // Lấy thông tin người dùng
	    User user = userRepository.findById(addressDTO.getId_user()).orElse(null);
	    if (user == null) {
	        return ResponseEntity.badRequest().body("User not found");
	    }

	    // Lấy danh sách địa chỉ của người dùng
	    List<Address> listAddresses = addressRepository.findByUserId(addressDTO.getId_user());

	    // Kiểm tra xem người dùng đã có địa chỉ mặc định chưa
	    boolean hasDefaultAddress = listAddresses.stream().anyMatch(Address::getIs_default);

	    // Tạo địa chỉ mới
	    Address address = new Address();
	    address.setUser(user);
	    address.setFull_address(addressDTO.getFulladdress());
	    address.setId_district(addressDTO.getDistrictId());
	    address.setId_province(addressDTO.getProvinceId());
	    address.setId_ward(addressDTO.getWardId());
	    
	    // Nếu chưa có địa chỉ mặc định thì set is_default = true, ngược lại là false
	    address.setIs_default(!hasDefaultAddress);
	    address.setIs_deleted(Boolean.FALSE);

	    // Lưu địa chỉ vào database
	    addressRepository.save(address);

	    return ResponseEntity.ok(address);
	}



	@PostMapping("detail/{id}")
	public ResponseEntity<Address> getAddressDetail(@PathVariable("id") Integer id) {
		Address address = addressRepository.findById(id).orElse(null);
		if (address == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(address);
	}

//	@DeleteMapping("delete/{id}")
//	public ResponseEntity<Address> deleteAdddress(@PathVariable("id") Integer id) {
//		Address address = addressRepository.findById(id).get();
//		addressRepository.delete(address);
//		return ResponseEntity.ok().build();
//	}
	
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteAddress(@PathVariable("id") Integer id) {

		System.out.println(id);
	    Address address = addressRepository.findById(id).get();
	    
	    if (address == null) {
	        return ResponseEntity.notFound().build();
	    }
	    
	    address.setId(id);
	    address.setIs_deleted(Boolean.TRUE);
	    address.setIs_default(Boolean.FALSE);
	 

	    // Nếu không có đơn hàng liên kết, thực hiện xóa địa chỉ
	    addressRepository.save(address);
	    return ResponseEntity.ok("Địa chỉ đã được xóa thành công.");
	}

	
	
	@GetMapping("/getAddress/{id}")
	public ResponseEntity<Address> getAddressFirst(@PathVariable("id") Integer id){
		Address address = addressRepository.findByDefault(id);
		return ResponseEntity.ok(address);
	}

	@PostMapping("/update")
	public ResponseEntity<String> updateAddress(@RequestBody AddressDTO addressDTO) {
		try {
			Address address = addressRepository.findById(addressDTO.getId_address()).orElse(null);
			if (address == null) {
				return ResponseEntity.notFound().build();
			}

			// Cập nhật thông tin địa chỉ
			address.setId_province(addressDTO.getProvinceId());
			address.setId_district(addressDTO.getDistrictId());
			address.setId_ward(addressDTO.getWardId());
			address.setFull_address(addressDTO.getFulladdress());
			address.setIs_default(address.getIs_default());
			// Lưu địa chỉ đã cập nhật vào cơ sở dữ liệu
			addressRepository.save(address);

			return ResponseEntity.ok("Address updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update address");
		}
	}
	
	
	@PostMapping("/set-default/{id}")
    public ResponseEntity<String> setDefaultAddress(@PathVariable Integer id) {
        try {
            // Lấy địa chỉ đã chọn theo id
            Address selectedAddress = addressRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));

            // Lấy danh sách tất cả các địa chỉ của người dùng
            List<Address> userAddresses = addressRepository.findByUserId(selectedAddress.getUser().getId());

            // Đặt tất cả các địa chỉ của người dùng về is_default = false
            for (Address address : userAddresses) {
                address.setIs_default(false);
            }

            // Đặt địa chỉ được chọn là mặc định (is_default = true)
            selectedAddress.setIs_default(true);

            // Lưu lại các thay đổi
            addressRepository.saveAll(userAddresses);
            return ResponseEntity.ok("Thiết lập địa chỉ mặc định thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi thiết lập địa chỉ mặc định.");
        }
    }

}
