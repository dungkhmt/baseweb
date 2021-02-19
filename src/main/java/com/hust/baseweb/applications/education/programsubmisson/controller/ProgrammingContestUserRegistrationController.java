package com.hust.baseweb.applications.education.programsubmisson.controller;

import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContestUserRegistration;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateProgrammingContestUserRegistrationInputModel;
import com.hust.baseweb.applications.education.programsubmisson.service.ProgrammingContestUserRegistrationService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

@Log4j2
@Controller
//@RequestMapping("/edu/programsubmission")
@AllArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin
public class ProgrammingContestUserRegistrationController {
    public static final String REGISTATION_STATUS_REGISTERED = "REGISTERED";
    public static final String REGISTATION_STATUS_APPROVED = "APPROVED";

    private ProgrammingContestUserRegistrationService programmingContestUserRegistrationService;
    private UserService userService;
    @PostMapping("register-programming-contest")
    public ResponseEntity<?> registerProgrammingContest(Principal principal, @RequestBody
                                                        CreateProgrammingContestUserRegistrationInputModel input){
        UserLogin userLogin = userService.findById(principal.getName());
        input.setUserLoginId(userLogin.getUserLoginId());
        input.setStatusId(REGISTATION_STATUS_REGISTERED);

        ProgrammingContestUserRegistration programmingContestUserRegistration = programmingContestUserRegistrationService.save(input);
        return ResponseEntity.ok().body(programmingContestUserRegistration);
    }

    @GetMapping("get-all-programming-contest-user-registration-list")
    public ResponseEntity<?> getAllProgrammingContestUserRegistrationList(Principal principal){
        List<ProgrammingContestUserRegistration> programmingContestUserRegistrationList = programmingContestUserRegistrationService.findAll();
        return ResponseEntity.ok().body(programmingContestUserRegistrationList);
    }
}
