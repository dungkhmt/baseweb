package com.hust.baseweb.applications.mail.service;

import com.hust.baseweb.config.MailProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @author Le Anh Tuan
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MailServiceImpl implements MailService {

    private JavaMailSender mailSender;

    private MailProperties properties;

    public SimpleMailMessage createSimpleMail(String[] to, String[] cc, String[] bcc, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(properties.getUsername());
        mailMessage.setTo(to);
        mailMessage.setCc(cc);
        mailMessage.setBcc(bcc);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailMessage.setSentDate(new Date());

        return mailMessage;
    }

    public SimpleMailMessage createSimpleMail(String[] to, String subject, String body) {
        return createSimpleMail(to, null, null, subject, body);
    }

    public void sendSimpleMail(String[] to, String[] cc, String[] bcc, String subject, String body) {
        mailSender.send(createSimpleMail(to, cc, bcc, subject, body));
    }

    public void sendSimpleMail(String[] to, String subject, String body) {
        sendSimpleMail(to, null, null, subject, body);
    }

    @Override
    public void sendMultipleSimpleMail(SimpleMailMessage... simpleMessages) throws MailException {
        mailSender.send(simpleMessages);
    }


    @Override
    public MimeMessage createMimeMessage(
        String[] to,
        String[] cc,
        String[] bcc,
        String subject,
        String body,
        String replyTo,
        MultipartFile[] files
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper =
                new MimeMessageHelper(message, true);

            messageHelper.setFrom(properties.getUsername());
            messageHelper.setTo("phamducdat2402@gmail.com");
//            messageHelper.setCc("mail@gmail.com");
//            messageHelper.setBcc("mail@gmail.com");
            messageHelper.setText("Test");
            for (MultipartFile file : files) {
                if(file != null) {
                    messageHelper.addAttachment(file.getOriginalFilename(), file, file.getContentType());
                }
            }
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void sendMailWithMultipleFile(
        String[] to,
        String[] cc,
        String[] bcc,
        String subject,
        String body,
        String replyTo,
        MultipartFile[] files
    ) {
        MimeMessage mimeMessage = createMimeMessage(to, cc, bcc, subject, body, replyTo, files);
        if (mimeMessage != null) {
            mailSender.send(mimeMessage);
        }
    }
}
