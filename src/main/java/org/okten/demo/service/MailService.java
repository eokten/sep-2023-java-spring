package org.okten.demo.service;

import lombok.RequiredArgsConstructor;
import org.okten.demo.dto.SendMailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSender mailSender;

    @Value("${mail.sender.username}")
    private String mailSenderUsername;

    public void sendMail(SendMailDto sendMailDto) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject(sendMailDto.getSubject());
        message.setText(sendMailDto.getText());

        message.setFrom(mailSenderUsername);
        message.setTo(sendMailDto.getRecipient());

        mailSender.send(message);
    }
}
