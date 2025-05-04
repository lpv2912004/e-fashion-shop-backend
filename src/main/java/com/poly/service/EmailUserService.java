package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;

@Service
public class EmailUserService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Gửi email thông báo cho người dùng với thông tin người dùng và cửa hàng.
     *
     * @param to             Địa chỉ email người nhận.
     * @param subject        Tiêu đề email.
     * @param customerName   Tên người dùng.
     * @param shopName       Tên cửa hàng.
     * @param shopAddress    Địa chỉ cửa hàng.
     * @param shopContact    Thông tin liên hệ cửa hàng (số điện thoại/email).
     * @param htmlContent    Nội dung email dưới dạng HTML.
     */
    public void sendUserNotificationEmail(String to, String subject, String customerName, String shopName,
                                          String shopAddress, String shopContact, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);

            // Cấu trúc email với thông tin người dùng và cửa hàng
            String emailContent = "<html>"
                    + "<body style='font-family: Arial, sans-serif;'>"
                    + "<h2>Thông báo cho khách hàng: " + customerName + "</h2>"
                    + "<p>Cảm ơn bạn đã lựa chọn cửa hàng " + shopName + ".</p>"
                    + "<p><strong>Thông tin cửa hàng:</strong></p>"
                    + "<p>Tên cửa hàng: " + shopName + "</p>"
                    + "<p>Địa chỉ cửa hàng: " + shopAddress + "</p>"
                    + "<p>Thông tin liên hệ: " + shopContact + "</p>"
                    + "<h3>Nội dung thông báo:</h3>"
                    + "<p>" + htmlContent + "</p>"
                    + "<p>Chúng tôi luôn sẵn sàng hỗ trợ bạn. Nếu có bất kỳ câu hỏi nào, đừng ngần ngại liên hệ với chúng tôi.</p>"
                    + "</body>"
                    + "</html>";

            helper.setText(emailContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

    /**
     * Gửi email với hỗ trợ đính kèm tệp tin cho người dùng.
     *
     * @param to             Địa chỉ email người nhận.
     * @param subject        Tiêu đề email.
     * @param customerName   Tên người dùng.
    * @param shopName       Tên cửa hàng.
     * @param shopAddress    Địa chỉ cửa hàng.
     * @param shopContact    Thông tin liên hệ cửa hàng.
     * @param htmlContent    Nội dung email dưới dạng HTML.
     * @param filePath       Đường dẫn tệp tin đính kèm.
     */
    public void sendUserEmailWithAttachment(String to, String subject, String customerName, String shopName,
                                            String shopAddress, String shopContact, String htmlContent, String filePath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);

            // Cấu trúc email với thông tin người dùng, cửa hàng và tệp đính kèm
            String emailContent = "<html>"
                    + "<body style='font-family: Arial, sans-serif;'>"
                    + "<h2>Thông báo cho khách hàng: " + customerName + "</h2>"
                    + "<p>Cảm ơn bạn đã lựa chọn cửa hàng " + shopName + ".</p>"
                    + "<p><strong>Thông tin cửa hàng:</strong></p>"
                    + "<p>Tên cửa hàng: " + shopName + "</p>"
                    + "<p>Địa chỉ cửa hàng: " + shopAddress + "</p>"
                    + "<p>Thông tin liên hệ: " + shopContact + "</p>"
                    + "<h3>Nội dung thông báo:</h3>"
                    + "<p>" + htmlContent + "</p>"
                    + "<p>Chúng tôi luôn sẵn sàng hỗ trợ bạn. Nếu có bất kỳ câu hỏi nào, đừng ngần ngại liên hệ với chúng tôi.</p>"
                    + "</body>"
                    + "</html>";

            helper.setText(emailContent, true);

            // Đính kèm tệp tin nếu có
            if (filePath != null && !filePath.isEmpty()) {
                helper.addAttachment("Attachment", new File(filePath));
            }

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email with attachment", e);
        }
    }
}