package com.hust.baseweb.applications.admin;

import com.hust.baseweb.applications.admin.model.SendMailToAllUsersInputModel;
import com.hust.baseweb.applications.mail.service.MailService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.entity.UserRegister;
import com.hust.baseweb.repo.UserRegisterRepo;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Log4j2
@RestController
@AllArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class AdminController {
    UserService userService;
    MailService mailService;
    UserRegisterRepo userRegisterRepo;

    @PostMapping("/send-mail-to-all-users")
    public ResponseEntity<?> sendMailToAllUsers(Principal principal, @RequestBody SendMailToAllUsersInputModel input){
        UserLogin u = userService.findById(principal.getName());
        log.info("sendMailToAllUsers, title = " + input.getMailTitle() + ", content = " + input.getMailContent());
        List<UserRegister> userRegisterList = userRegisterRepo.findAll();
        //String[] to = null;
        String replyTo = "sscm40@gmail.com";
        for(UserRegister ur: userRegisterList) {
            String to = ur.getEmail();
            String[] toEmails = {to};
            SimpleMailMessage simpleMailMessage = mailService.createSimpleMail(
                toEmails,
                input.getMailTitle(),
                input.getMailContent(),
                replyTo);
            log.info("sendMailToAllUsers, sent OK to " + to);
        }
        return ResponseEntity.ok().body("OK");
    }
}
