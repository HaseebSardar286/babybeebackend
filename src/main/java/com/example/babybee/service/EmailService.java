package com.example.babybee.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

/**
 * Service for sending email notifications.
 */
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    /**
     * Sends a secure password reset code to the specified user email.
     *
     * @param toEmail the recipient's email address
     * @param code    the secure 6-digit verification code
     */
    public void sendResetToken(String toEmail, String code) {
        String resetLink = "http://localhost:3000/reset-password?code=" + code;
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            
            helper.setFrom("haseebsardar123123@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Reset Your Password - BabyBee 🐝");
            
            // Clean, beautifully branded HTML email keeping raw tokens fully hidden from plain view
            String htmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 550px; margin: 0 auto; padding: 30px; border: 1px solid #f0e6df; border-radius: 24px; background-color: #ffffff;\">"
                + "<div style=\"text-align: center; margin-bottom: 24px;\">"
                + "  <div style=\"width: 60px; height: 60px; line-height: 60px; background-color: #fdf5e6; border-radius: 20px; font-size: 32px; display: inline-block;\">🐝</div>"
                + "  <h2 style=\"color: #2d3748; margin-top: 12px; font-weight: 700;\">BabyBee</h2>"
                + "</div>"
                + "<p style=\"color: #4a5568; font-size: 15px; line-height: 1.6;\">Hello,</p>"
                + "<p style=\"color: #4a5568; font-size: 15px; line-height: 1.6;\">We received a request to reset the password for your BabyBee account. Please click the button below to choose a new password:</p>"
                + "<div style=\"text-align: center; margin: 30px 0;\">"
                + "  <a href=\"" + resetLink + "\" style=\"background-color: #e57373; color: #ffffff; padding: 12px 36px; border-radius: 9999px; text-decoration: none; font-weight: 600; font-size: 14px; display: inline-block; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);\">Reset Password</a>"
                + "</div>"
                + "<p style=\"color: #718096; font-size: 13px; line-height: 1.6;\">If you did not request a password reset, you can safely ignore this email; your credentials remain secure.</p>"
                + "<hr style=\"border: none; border-top: 1px solid #edf2f7; margin: 24px 0;\">"
                + "<p style=\"color: #a0aec0; font-size: 11px; text-align: center; line-height: 1.5;\">Best regards,<br>The BabyBee Team</p>"
                + "</div>";
            
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            System.out.println("SUCCESSFULLY SENT PASSWORD RESET EMAIL TO: " + toEmail);
        } catch (Exception e) {
            System.err.println("WARNING: Failed to send email via SMTP (" + e.getMessage() + ").");
            System.err.println("--------------------------------------------------------------------------------");
            System.err.println("[DEVELOPMENT FALLBACK] Secure Password Reset Link for " + toEmail + ":");
            System.err.println(resetLink);
            System.err.println("--------------------------------------------------------------------------------");
            throw new RuntimeException("Failed to send email via SMTP (Mail server connection failed).", e);
        }
    }
}
