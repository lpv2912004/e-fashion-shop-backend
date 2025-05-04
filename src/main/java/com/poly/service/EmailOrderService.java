package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Async;

@Service
public class EmailOrderService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailOrderService.class);

    @Async
    public void sendOrderCancellationEmail(String toEmail, String orderId, String reason, String customerName, 
                                           String shippingAddress, double deliveryFee, double totalCash, 
                                           List<String> productDetails) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("MAOU - Thông báo hủy đơn hàng #" + orderId);

            StringBuilder productsHtml = new StringBuilder();
            for (String detail : productDetails) {
                productsHtml.append("<li>").append(detail).append("</li>");
            }

            String htmlContent = "<html>"
                    + "<head>"
                    + "<style>"
                    + "body {"
                    + "    font-family: Arial, sans-serif;"
                    + "    line-height: 1.6;"
                    + "    color: #333333;"
                    + "    background-color: #f9f9f9;"
                    + "    margin: 0;"
                    + "    padding: 20px;"
                    + "}"
                    + "h2 {"
                    + "    color: #2c3e50;"
                    + "    border-bottom: 2px solid #e74c3c;"
                    + "    padding-bottom: 10px;"
                    + "    margin-bottom: 20px;"
                    + "}"
                    + "p {"
                    + "    margin: 5px 0;"
                    + "}"
                    + "ul {"
                    + "    list-style-type: disc;"
                    + "    margin: 10px 20px;"
                    + "    padding: 0;"
                    + "}"
                    + "li {"
                    + "    margin: 5px 0;"
                    + "    line-height: 1.4;"
                    + "}"
                    + ".details {"
                    + "    background-color: #ffffff;"
                    + "    border: 1px solid #dddddd;"
                    + "    border-radius: 5px;"
                    + "    padding: 20px;"
                    + "    margin-bottom: 20px;"
                    + "}"
                    + ".footer {"
                    + "    font-size: 0.9em;"
                    + "    color: #666666;"
                    + "    margin-top: 20px;"
                    + "    border-top: 1px solid #dddddd;"
                    + "    padding-top: 10px;"
                    + "    text-align: center;"
                    + "}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
             
                    + "<h2>Đơn hàng #" + orderId + " đã bị hủy</h2>"	
                    + "<div class='details'>"
                    + "    <p><strong>Khách hàng:</strong> " + customerName + "</p>"
                    + "    <p><strong>Địa chỉ giao hàng:</strong> " + shippingAddress + "</p>"
                    + "    <p><strong>Lý do hủy:</strong> " + reason + "</p>"
                    + "</div>"
                    + "<h3>Chi tiết đơn hàng:</h3>"
                    + "<ul>" + productsHtml.toString() + "</ul>"
                    + "<div class='details'>"
                    + "    <p><strong>Phí vận chuyển:</strong> " + deliveryFee + " VND</p>"
                    + "    <p><strong>Tổng thanh toán:</strong> " + totalCash + " VND</p>"
                    + "</div>"
                    + "<p>Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi.</p>"
                    + "<div class='footer'>"
                    + "    <p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>"
                    + "    <p>&copy; 2024 Công ty TNHH MAOU. Tất cả các quyền được bảo lưu.</p>"
                    + "</div>"
                    + "</body>"
                    + "</html>";


            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);

            logger.info("Đã gửi email hủy đơn hàng tới " + toEmail);

        } catch (MessagingException e) {
            logger.error("Lỗi khi gửi email hủy đơn hàng tới " + toEmail, e);
        }
    }
}
