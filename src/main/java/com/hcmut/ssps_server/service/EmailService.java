package com.hcmut.ssps_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setFrom("ssps.hcmut.241@gmail.com");
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
    }

    public String generateEmailContent(String fullName, String fileName) {
        return "Gửi " + fullName + ",\n\n" +
                "Yêu cầu in tài liệu " + fileName + " của bạn đã được hoàn tất.\n\n" +
                "Xin vui lòng đến phòng in để nhận tài liệu.\n\n" +
                "Xin chân thành cảm ơn!";

    }

    public String generateEmailSubject(String fileName) {
        return "Hoàn tất yêu cầu in tài liệu " + fileName;
    }
}
