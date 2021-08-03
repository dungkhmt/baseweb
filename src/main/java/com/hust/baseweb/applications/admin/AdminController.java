package com.hust.baseweb.applications.admin;

import com.hust.baseweb.applications.admin.model.SendMailToAllUsersInputModel;
import com.hust.baseweb.applications.mail.service.MailService;
import com.hust.baseweb.config.MailProperties;
import com.hust.baseweb.repo.UserLoginRepo;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Log4j2
@RestController
@AllArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class AdminController {

    UserService userService;

    MailService mailService;

    UserLoginRepo userLoginRepo;

    private MailProperties properties;

    @PostMapping("/send-mail-to-all-users")
    public ResponseEntity<?> sendMailToAllUsers(@RequestBody SendMailToAllUsersInputModel input) {
        log.info("sendMailToAllUsers, title = " +
                 input.getMailTitle() +
                 ", content = " +
                 input.getMailContent());

        String replyTo = properties.getUsername();
        List<String> toEmails = userLoginRepo.findAllUserEmail();
        List<SimpleMailMessage> mails = new ArrayList<>();

        for (String email : toEmails) {
            if (null != email) {
                log.info("sendMailToAllUsers, prepare to send email to " + email);
                SimpleMailMessage mail = mailService.createSimpleMail(
                    new String[]{email},
                    input.getMailTitle(),
                    input.getMailContent(),
                    replyTo);

                mails.add(mail);
            }
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            mailService.sendMultipleSimpleMail(mails.toArray(new SimpleMailMessage[]{}));
        });

        return ResponseEntity.ok().body("OK");
    }
}
