package com.hust.baseweb.applications.mail.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
