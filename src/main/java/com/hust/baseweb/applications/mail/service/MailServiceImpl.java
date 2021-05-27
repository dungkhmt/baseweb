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

import javax.mail.MessagingException;
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

    public SimpleMailMessage createSimpleMail(
        String[] to,
        String[] cc,
        String[] bcc,
        String subject,
        String body,
        String replyTo
    ) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(properties.getUsername());
        mailMessage.setTo(to);
        mailMessage.setCc(cc);
        mailMessage.setBcc(bcc);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailMessage.setSentDate(new Date());
        mailMessage.setReplyTo(replyTo);

        return mailMessage;
    }

    public SimpleMailMessage createSimpleMail(String[] to, String subject, String body, String replyTo) {
        return createSimpleMail(to, null, null, subject, body, replyTo);
    }

    public void sendSimpleMail(String[] to, String[] cc, String[] bcc, String subject, String body, String replyTo) {
        mailSender.send(createSimpleMail(to, cc, bcc, subject, body, replyTo));
    }

    public void sendSimpleMail(String[] to, String subject, String body, String replyTo) {
        sendSimpleMail(to, null, null, subject, body, replyTo);
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
        MultipartFile[] attachments
    ) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

        messageHelper.setFrom(properties.getUsername());
        messageHelper.setTo(to);

        if (null != cc && cc.length > 0) {
            messageHelper.setCc(cc);
        }

        if (null != bcc && bcc.length > 0) {
            messageHelper.setBcc(bcc);
        }

        messageHelper.setSubject(subject);
        messageHelper.setText(body); // TODO: research 2 overloading methods
        messageHelper.setReplyTo(replyTo);
        messageHelper.setSentDate(new Date());

//        Research 5 following methods
//        messageHelper.setFrom(properties.getUsername(), "personal");
//        messageHelper.setPriority();
//        messageHelper.setFileTypeMap()
//        messageHelper.setValidateAddresses();
//        messageHelper.setReplyTo(replyTo, "personal");

        // FIXME: Exception when no file has been chosen in the multipart form.
        for (MultipartFile attachment : attachments) {
            if (null != attachment) {
                messageHelper.addAttachment(attachment.getOriginalFilename(), attachment, attachment.getContentType());
            }
        }

        return message;
    }

    @Override
    public void sendMailWithAttachments(
        String[] to,
        String[] cc,
        String[] bcc,
        String subject,
        String body,
        String replyTo,
        MultipartFile[] attachments
    ) {
        try {
            MimeMessage mimeMessage = createMimeMessage(to, cc, bcc, subject, body, replyTo, attachments);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
