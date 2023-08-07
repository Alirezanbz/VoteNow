package com.example.votenow.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Random;
import java.util.UUID;

@Service
public class MailSenderService {

    private String verificationCode;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("nabavizadehseyedalireza@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(mimeMessage);
            System.out.println("Mail sent successfully!");

        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateVerificationCode() {
        int codeLength = 6;
        String characters = "0123456789";
        StringBuilder code = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        verificationCode = code.toString();
    }
    public void sendVerificationEmail(String toEmail) throws Exception {
        generateVerificationCode();
        String subject = "Email Verification Test";
        String htmlContent = "<p>Your verification code: " + verificationCode + "</p>";
        sendEmail(toEmail, subject, htmlContent);
    }

    public void sendResetPasswordEmail(String toEmail, String token) throws Exception {
        String subject = "Reset Password";
        String htmlContent = "<p>Please click the link below to reset your password:</p>"
                + "<a href=\"http://localhost:8080/reset-password?token=" + token + "\">Reset Password</a>";
        sendEmail(toEmail, subject, htmlContent);
    }

    public String generateToken(){
        return UUID.randomUUID().toString();
    }
    public String getVerificationCode() {
        return verificationCode;
    }


}
