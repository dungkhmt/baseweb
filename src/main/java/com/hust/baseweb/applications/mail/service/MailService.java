package com.hust.baseweb.applications.mail.service;

import org.springframework.mail.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
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
     * @param replyTo nullable
     * @return
     */
    SimpleMailMessage createSimpleMail(
        String[] to,
        String[] cc,
        String[] bcc,
        String subject,
        String body,
        String replyTo
    );

    /**
     * Create simple mail message.
     *
     * @param to      receivers' mail address, should not be empty
     * @param subject nullable
     * @param body    nullable
     * @param replyTo nullable
     * @return
     */
    SimpleMailMessage createSimpleMail(String[] to, String subject, String body, String replyTo);

    /**
     * Send simple mail message.
     *
     * @param to      receivers' mail address, should not be empty
     * @param cc      nullable
     * @param bcc     nullable
     * @param subject nullable
     * @param body    nullable
     * @param replyTo nullable
     * @throws MailParseException          in case of failure when parsing the message
     * @throws MailAuthenticationException in case of authentication failure
     * @throws MailSendException           in case of failure when sending the message
     */
    void sendSimpleMail(String[] to, String[] cc, String[] bcc, String subject, String body, String replyTo);

    /**
     * Send simple mail message.
     *
     * @param to      receivers' mail address, should not be empty
     * @param subject nullable
     * @param body    nullable
     * @param replyTo nullable
     * @throws MailParseException          in case of failure when parsing the message
     * @throws MailAuthenticationException in case of authentication failure
     * @throws MailSendException           in case of failure when sending the message
     */
    void sendSimpleMail(String[] to, String subject, String body, String replyTo);

    /**
     * Send the given array of simple mail messages in batch.
     *
     * @param simpleMessages the messages to send
     * @throws MailParseException          in case of failure when parsing a message
     * @throws MailAuthenticationException in case of authentication failure
     * @throws MailSendException           in case of failure when sending a message
     */
    void sendMultipleSimpleMail(SimpleMailMessage... simpleMessages) throws MailException;

    /**
     * Create mime message.
     *
     * @param to      receivers' mail address, should not be empty
     * @param cc      nullable
     * @param bcc     nullable
     * @param subject nullable
     * @param body    nullable
     * @param replyTo nullable
     * @param attachments
     * @return
     */
    MimeMessage createMimeMessage(
        String[] to, String[] cc, String[] bcc, String subject, String body, String replyTo, MultipartFile[] attachments
    ) throws MessagingException;

    /**
     * Send a JavaMail MIME message.
     *
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param body
     * @param replyTo
     * @param attachments
     * @throws org.springframework.mail.MailAuthenticationException in case of authentication failure
     * @throws org.springframework.mail.MailSendException           in case of failure when sending the message
     */
    void sendMailWithAttachments(
        String[] to, String[] cc, String[] bcc, String subject, String body, String replyTo, MultipartFile[] attachments
    );
}
