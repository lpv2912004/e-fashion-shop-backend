
package com.poly.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class OtpService {
    private static final int OTP_EXPIRATION_MINUTES = 2; // OTP valid for 2 minutes
    private static final int OTP_LENGTH = 6; // OTP length
    private final SecureRandom random = new SecureRandom(); // Secure random generator
    private final Map<String, OtpEntry> otpStore = new HashMap<>(); // Store OTPs in memory
    private final Map<Integer, OtpVerificationEntry> otpVerificationCache = new HashMap<>(); // Store email verification status and expiration time by userId

    // Generate OTP for a specific email
    public String generateOtp(String email) {
        String otp = String.format("%06d", random.nextInt(1000000)); // Generate 6-digit OTP
        otpStore.put(email, new OtpEntry(otp, LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES))); // Save OTP with expiration time
        return otp;
    }

    // Check if the OTP is valid for a given email
    public boolean isOtpValid(String email, String otp) {
        OtpEntry entry = otpStore.get(email);
        if (entry == null || LocalDateTime.now().isAfter(entry.getExpirationTime())) {
            resetEmailVerificationFlagByEmail(email); // Clear verification flag if OTP has expired
            return false; // OTP is expired or not found
        }
        return entry.getOtp().equals(otp); // Check if OTP matches
    }

    // Invalidate OTP after use
    public void invalidateOtp(String email) {
        otpStore.remove(email); // Remove OTP from memory
    }

    // Mark email as verified for update with expiration time (2 minutes)
    public void markEmailVerifiedForUpdate(Integer userId) {
        otpVerificationCache.put(userId, new OtpVerificationEntry(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES)));
    }

    // Check if the email is verified for an update
    public boolean isEmailVerifiedForUpdate(Integer userId) {
        OtpVerificationEntry entry = otpVerificationCache.get(userId);
        if (entry == null || LocalDateTime.now().isAfter(entry.getExpirationTime())) {
            resetEmailVerificationFlag(userId); // Reset if the verification time expired
            return false; // Not verified or expired
        }
        return true; // Email is verified and valid
    }

    // Reset the verification flag after successful update
    public void resetEmailVerificationFlag(Integer userId) {
        otpVerificationCache.remove(userId);
    }

    // Reset verification flag based on email if OTP is expired
    private void resetEmailVerificationFlagByEmail(String email) {
        otpVerificationCache.values().removeIf(entry -> entry.getExpirationTime().isBefore(LocalDateTime.now())); // Reset expired entries
    }

    // Helper class to store OTP and expiration time
    private static class OtpEntry {
        private final String otp;
        private final LocalDateTime expirationTime;

        public OtpEntry(String otp, LocalDateTime expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }

    // Helper class to store email verification status and expiration time
    private static class OtpVerificationEntry {
        private final LocalDateTime expirationTime;

        public OtpVerificationEntry(LocalDateTime expirationTime) {
            this.expirationTime = expirationTime;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }
}


