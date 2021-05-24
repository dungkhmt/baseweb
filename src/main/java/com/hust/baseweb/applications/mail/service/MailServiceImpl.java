package com.hust.baseweb.applications.mail.service;

import com.hust.baseweb.config.MailProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Le Anh Tuan
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MailServiceImpl implements MailService {

    private JavaMailSender mailSender;

    private MailProperties properties;

    public void sendSimpleMail(String[] to, String[] cc, String[] bcc, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(properties.getUsername());
        mailMessage.setTo(to);
        mailMessage.setCc(cc);
        mailMessage.setBcc(bcc);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailMessage.setSentDate(new Date());
//        mailMessage.setReplyTo(properties.getUsername());

        mailSender.send(mailMessage);
    }

    public void sendSimpleMail(String[] to, String subject, String body) {
        sendSimpleMail(to, null, null, subject, body);
    }
}
