package com.poly.user;


import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.config.VNPayConfig;
import com.poly.dto.CheckoutElement;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user/api")
public class VNPayController {
    
	 @Autowired
	    private VNPayConfig vnpayConfig;

	    @PostMapping("/payment")
	    public String checkout(@RequestBody Map<String, Object> paymentData) throws UnsupportedEncodingException {
	        String amountStr = paymentData.get("amount") != null ? paymentData.get("amount").toString() : "0";
	        String orderType = paymentData.get("orderType") != null ? paymentData.get("orderType").toString() : "";
	        String returnUrl = paymentData.get("returnUrl") != null ? paymentData.get("returnUrl").toString() : "";

	        if (orderType.isEmpty() || returnUrl.isEmpty()) {
	            throw new IllegalArgumentException("Thiếu dữ liệu cần thiết để thực hiện thanh toán");
	        }

	        String vnp_Version = "2.1.0";
	        String vnp_Command = "pay";
	        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
	        String vnp_IpAddr = "127.0.0.1";

	        Map<String, String> vnp_Params = new HashMap<>();
	        vnp_Params.put("vnp_Version", vnp_Version);
	        vnp_Params.put("vnp_Command", vnp_Command);
	        vnp_Params.put("vnp_TmnCode", vnpayConfig.getVnp_TmnCode());
	        vnp_Params.put("vnp_Amount", String.valueOf((int) (Double.parseDouble(amountStr) * 100)));
	        vnp_Params.put("vnp_CurrCode", "VND");
	        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
	        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
	        vnp_Params.put("vnp_OrderType", orderType);
	        vnp_Params.put("vnp_Locale", "vn");
	        vnp_Params.put("vnp_ReturnUrl", returnUrl);
	        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

	        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	        String vnp_CreateDate = formatter.format(cld.getTime());
	        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
	        cld.add(Calendar.MINUTE, 15);
	        String vnp_ExpireDate = formatter.format(cld.getTime());
	        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

	        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
	        Collections.sort(fieldNames);
	        StringBuilder hashData = new StringBuilder();
	        StringBuilder query = new StringBuilder();
	        Iterator<String> itr = fieldNames.iterator();
	        while (itr.hasNext()) {
	            String fieldName = itr.next();
	            String fieldValue = vnp_Params.get(fieldName);
	            if (fieldValue != null && fieldValue.length() > 0) {
	                hashData.append(fieldName);
	                hashData.append('=');
	                hashData.append(URLEncoder.encode(fieldValue, "UTF-8"));
	                query.append(URLEncoder.encode(fieldName, "UTF-8"));
	                query.append('=');
	                query.append(URLEncoder.encode(fieldValue, "UTF-8"));
	                if (itr.hasNext()) {
	                    query.append('&');
	                    hashData.append('&');
	                }
	            }
	        }
	        String queryUrl = query.toString();

	        String vnp_SecureHash = HmacUtils.hmacSha512Hex(vnpayConfig.getVnp_HashSecret(), hashData.toString());
	        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
	        String paymentUrl = vnpayConfig.getVnp_Url() + "?" + queryUrl;
	        
	        // In ra các thông tin để kiểm tra
	        System.out.println("Calculated SecureHash: " + vnp_SecureHash);
	        System.out.println("Payment URL: " + paymentUrl);
	        
	        return paymentUrl;
	    }

	    @Transactional
	    @GetMapping("/k1")
	    public String paymentResult(HttpServletRequest request) throws UnsupportedEncodingException {
	        Map<String, String> vnp_Params = new HashMap<>();
	        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
	            String param = params.nextElement();
	            String value = request.getParameter(param);
	            if ((value != null) && (value.length() > 0)) {
	                vnp_Params.put(param, value);
	            }
	        }

	        String vnp_SecureHash = request.getParameter("vnp_SecureHash");

	        if (vnp_Params.containsKey("vnp_SecureHashType")) {
	            vnp_Params.remove("vnp_SecureHashType");
	        }
	        if (vnp_Params.containsKey("vnp_SecureHash")) {
	            vnp_Params.remove("vnp_SecureHash");
	        }

	        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
	        Collections.sort(fieldNames);
	        StringBuilder hashData = new StringBuilder();
	        for (String fieldName : fieldNames) {
	            String fieldValue = vnp_Params.get(fieldName);
	            if ((fieldValue != null) && (fieldValue.length() > 0)) {
	                hashData.append(fieldName);
	                hashData.append('=');
	                hashData.append(URLEncoder.encode(fieldValue, "UTF-8"));
	                hashData.append('&');
	            }
	        }
	        if (hashData.length() > 0) {
	            hashData.deleteCharAt(hashData.length() - 1);
	        }

	        String secureHash = HmacUtils.hmacSha512Hex(vnpayConfig.getVnp_HashSecret(), hashData.toString());

	        if (secureHash.equals(vnp_SecureHash)) {
	            String responseCode = request.getParameter("vnp_ResponseCode");
	            if ("00".equals(responseCode)) {
	                System.out.println("Giao dịch thành công");
	            } else {
	                System.out.println("Giao dịch không thành công. Mã lỗi: " + responseCode);
	            }

	        } else {
	            System.out.println("Giao dịch không thành công. Chữ ký không khớp.");
	        }

	        return "redirect:/k";
	    }
}

