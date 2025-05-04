package com.poly.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class  TemporaryTokenStore {

    private final Map<String, TokenInfo> tokenStore = new ConcurrentHashMap<>();

    // Tạo token và lưu trữ cùng với thời gian hết hạn
    public String createToken(String email) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, new TokenInfo(email, LocalDateTime.now().plusMinutes(10))); // Token có thời gian hiệu lực 10 phút
        return token;
    }

    // Lấy thông tin TokenInfo dựa trên token
    public TokenInfo getTokenInfo(String token) {
        return tokenStore.get(token);
    }

    // Xóa token sau khi sử dụng
    public void removeToken(String token) {
        tokenStore.remove(token);
    }

    // Lớp lưu trữ thông tin token và email liên kết
    public static class TokenInfo { // Thay đổi thành public để truy cập từ bên ngoài
        private final String email;
        private final LocalDateTime expiryDate;

        public TokenInfo(String email, LocalDateTime expiryDate) {
            this.email = email;
            this.expiryDate = expiryDate;
        }

        public String getEmail() {
            return email;
        }

        public LocalDateTime getExpiryDate() {
            return expiryDate;
        }
    }
}

