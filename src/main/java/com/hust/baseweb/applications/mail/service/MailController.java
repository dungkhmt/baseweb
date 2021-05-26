package com.hust.baseweb.applications.mail.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
/**
 * @author Le Anh Tuan
 */
public class MailController {

    MailService mailService;

    @GetMapping("/simple-mail")
    void send(@RequestBody String[] to) {
        mailService.sendSimpleMail(to, null, null, "test", "test from openerp");
    }

    @PostMapping("/send-mail-with-multiple-files")
    public void sendMailWithMultipleFiles (@RequestParam("files") MultipartFile[] files) {
        String[] to = {"mail@gmail.com"};
        String body = "body";
        mailService.sendMailWithMultipleFile(to,null,null,null,body,"", files);
    }
}
