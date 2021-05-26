package com.hust.baseweb.applications.mail.service;

import org.springframework.mail.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;

/**
 * @author Le Anh Tuan
 */
public interface MailService {

    /**
     * Create simple mail message.
     *
     * @param to      receivers' mail address, should not be empty
     * @param cc      nullable
     * @param bcc     nullable
     * @param subject nullable
     * @param body    nullable
     * @return
     */
    SimpleMailMessage createSimpleMail(String[] to, String[] cc, String[] bcc, String subject, String body);

    /**
     * Create simple mail message.
     *
     * @param to      receivers' mail address, should not be empty
     * @param subject nullable
     * @param body    nullable
     * @return
     */
    SimpleMailMessage createSimpleMail(String[] to, String subject, String body);

    /**
     * Send simple mail message.
     *
     * @param to      receivers' mail address, should not be empty
     * @param cc      nullable
     * @param bcc     nullable
     * @param subject nullable
     * @param body    nullable
     * @throws MailParseException          in case of failure when parsing the message
     * @throws MailAuthenticationException in case of authentication failure
     * @throws MailSendException           in case of failure when sending the message
     */
    void sendSimpleMail(String[] to, String[] cc, String[] bcc, String subject, String body);

    /**
     * Send simple mail message.
     *
     * @param to      receivers' mail address, should not be empty
     * @param subject nullable
     * @param body    nullable
     * @throws MailParseException          in case of failure when parsing the message
     * @throws MailAuthenticationException in case of authentication failure
     * @throws MailSendException           in case of failure when sending the message
     */
    void sendSimpleMail(String[] to, String subject, String body);

    /**
     * Send the given array of simple mail messages in batch.
     *
     * @param simpleMessages the messages to send
     * @throws MailParseException          in case of failure when parsing a message
     * @throws MailAuthenticationException in case of authentication failure
     * @throws MailSendException           in case of failure when sending a message
     */
    void sendMultipleSimpleMail(SimpleMailMessage... simpleMessages) throws MailException;

    MimeMessage createMimeMessage(String[] to, String[] cc,
                           String[] bcc, String subject, String body, String replyTo, MultipartFile[] files);

    void sendMailWithMultipleFile(String[] to, String[] cc,
                                  String[] bcc, String subject, String body, String replyTo, MultipartFile[] files);
}
