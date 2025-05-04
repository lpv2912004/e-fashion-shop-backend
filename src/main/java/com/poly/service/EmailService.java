package com.poly.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String toEmail, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(toEmail);
            helper.setSubject("Mã OTP Của Shop MAOU");

            // HTML Content
            String htmlContent = "<html>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 10px;'>"
                    + "<div style='text-align: center; padding: 10px;'>"
                    + "<img src='YOUR_LOGO_URL' alt='Logo' style='width: 80px; height: auto;' />"
                    + "<h2 style='color: #333;'>Mã OTP Của Bạn</h2>"
                    + "</div>"

                    + "<div style='text-align: center; background-color: #f7f7f7; padding: 20px; border-radius: 5px;'>"
                    + "<p style='color: #555;'>Đây là mã phê duyệt đăng nhập của bạn:</p>"
                    + "<h1 style='font-size: 40px; letter-spacing: 5px; color: #000;'>" + otp + "</h1>"
                    + "<p style='color: #555;'>Mã sẽ hết hạn trong 1 phút</p>"
                    + "</div>"

                    + "<p style='text-align: center; color: #888;'>"
                    + "Nếu bạn không phải là người gửi yêu cầu này, hãy đổi mật khẩu tài khoản ngay lập tức để tránh việc bị truy cập trái phép."
                    + "</p>"

                    + "<div style='text-align: center; margin-top: 20px;'>"
                    + "<a href='#' style='color: #0073e6; text-decoration: none;'>Bảo Mật Tài Khoản</a>"
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

            helper.setText(htmlContent, true); // Enable HTML content
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle any exceptions
        }
    }

    public void sendOtpregis(String toEmail, String otp) {
        // Tạo email với HTML
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Mã xác nhận đăng ký tài khoản");

            // Tải template HTML và thay thế OTP vào trong template
            String htmlContent = getOtpEmailTemplate(otp);
            helper.setText(htmlContent, true);  // true để gửi email dạng HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();  // Xử lý lỗi nếu xảy ra
        }
    }

    private String getOtpEmailTemplate(String otp) {
        // Template HTML cho email
        String template = "<!DOCTYPE html>"
                + "<html><head><style>"
                + "body {font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;}"
                + ".email-container {width: 100%; background-color: #f4f4f4; padding: 20px;}"
                + ".email-content {max-width: 600px; background-color: #ffffff; margin: 0 auto; padding: 20px;"
                + "border-radius: 10px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);}"
                + ".email-header {background-color: #4f46e5; padding: 20px; text-align: center; border-radius: 10px 10px 0 0;}"
                + ".email-header h1 {color: #ffffff; margin: 0; font-size: 24px;}"
                + ".email-body {padding: 20px; text-align: center;}"
                + ".otp-code {font-size: 48px; font-weight: bold; color: #333333; margin: 20px 0;}"
                + ".email-footer {padding: 10px; text-align: center; font-size: 12px; color: #999999;}"
                + ".footer-text {margin: 5px 0;}"
                + ".support-link {color: #4f46e5; text-decoration: none;}"
                + "</style></head><body>"
                + "<div class='email-container'><div class='email-content'>"
                + "<div class='email-header'><h1>MÃ XÁC THỰC</h1></div>"
                + "<div class='email-body'>"
                + "<p>Thân gửi người dùng,</p>"
                + "<p>Mã xác nhận của bạn là:</p>"
                + "<div class='otp-code'>" + otp + "</div>"
                + "<p>Mã xác nhận sẽ hết hạn trong 1 phút.</p>"
                + "</div>"
                + "<div class='email-footer'>"
                + "<p class='footer-text'>Cảm ơn bạn đã sử dụng dịch vụ.</p>"
                + "<p class='footer-text'>Đây là Email gửi tự động. Vui lòng không phản hồi Email này.</p>"
                + "<p class='footer-text'>For support, please visit our <a class='support-link' href='https://yourwebsite.com/support'>support page</a>.</p>"
                + "</div></div></div></body></html>";

        return template;
    }
    
    
 // Hàm mới để gửi liên kết đặt lại mật khẩu
    public void sendResetPasswordEmail(String toEmail, String resetPasswordLink) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(toEmail);
            helper.setSubject("Yêu cầu đặt lại mật khẩu");

            // HTML Content cho email đặt lại mật khẩu
            String htmlContent = "<html>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 10px;'>"
                    + "<div style='text-align: center; padding: 10px;'>"
                    + "<img src='YOUR_LOGO_URL' alt='Logo' style='width: 80px; height: auto;' />"
                    + "<h2 style='color: #333;'>Yêu cầu đặt lại mật khẩu</h2>"
                    + "</div>"

                    + "<div style='text-align: center; background-color: #f7f7f7; padding: 20px; border-radius: 5px;'>"
                    + "<p style='color: #555;'>Bạn đã yêu cầu đặt lại mật khẩu. Vui lòng nhấp vào liên kết dưới đây để đặt lại mật khẩu của bạn:</p>"
                    + "<a href='" + resetPasswordLink + "' style='color: #0073e6; text-decoration: none;'>"+resetPasswordLink+"</a>"
                    + "</div>"

                    + "<p style='text-align: center; color: #888;'>"
                    + "Nếu bạn không yêu cầu điều này, hãy bỏ qua email này."
                    + "</p>"

                    + "<footer style='text-align: center; margin-top: 20px;'>"
                    + "<p style='color: #888;'>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi</p>"
                    + "</footer>"

                    + "</div>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true); // Cho phép nội dung HTML
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ nếu có
        }
    }
}
