package com.hust.baseweb.applications.mail.service;

/**
 * @author Le Anh Tuan
 */
public interface MailService {

    void sendSimpleMail(String[] to, String[] cc, String[] bcc, String subject, String body);

    void sendSimpleMail(String[] to, String subject, String body);
}
