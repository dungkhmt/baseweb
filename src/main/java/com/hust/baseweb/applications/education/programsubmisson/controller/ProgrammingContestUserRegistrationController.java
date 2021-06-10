package com.hust.baseweb.applications.education.programsubmisson.controller;

import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContestUserRegistration;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateProgrammingContestUserRegistrationInputModel;
import com.hust.baseweb.applications.education.programsubmisson.model.SearchProgrammingContestUserRegistrationInputModel;
import com.hust.baseweb.applications.education.programsubmisson.service.ProgrammingContestUserRegistrationService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Log4j2
@Controller
//@RequestMapping("/edu/programsubmission")
@AllArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin
public class ProgrammingContestUserRegistrationController {

    private ProgrammingContestUserRegistrationService programmingContestUserRegistrationService;
    private UserService userService;

    @PostMapping("search-programming-contest-user-registration")
    public ResponseEntity<?> searchProgrammingContestUserRegistration(
        Principal principal, @RequestBody
        SearchProgrammingContestUserRegistrationInputModel input
    ) {
        List<ProgrammingContestUserRegistration> programmingContestUserRegistrations = programmingContestUserRegistrationService
            .search(input);
        return ResponseEntity.ok().body(programmingContestUserRegistrations);
    }

    @PostMapping("register-programming-contest")
    public ResponseEntity<?> registerProgrammingContest(
        Principal principal, @RequestBody
        CreateProgrammingContestUserRegistrationInputModel input
    ) {
        UserLogin userLogin = userService.findById(principal.getName());
        log.info("registerProgrammingContest, contestId = " + input.getContestId());

        input.setUserLoginId(userLogin.getUserLoginId());
        input.setStatusId(ProgrammingContestUserRegistration.REGISTATION_STATUS_REGISTERED);

        ProgrammingContestUserRegistration programmingContestUserRegistration = programmingContestUserRegistrationService
            .save(input);
        return ResponseEntity.ok().body(programmingContestUserRegistration);
    }

    @PostMapping("approve-programming-contest-registration")
    public ResponseEntity<?> approveProgrammingContestRegistration(
        Principal principal, @RequestBody
        CreateProgrammingContestUserRegistrationInputModel input
    ) {
        UserLogin userLogin = userService.findById(principal.getName());
        log.info("approveProgrammingContestRegistration, contestId = " +
                 input.getContestId() +
                 " userLoginId = " +
                 input.getUserLoginId());

        //input.setUserLoginId(userLogin.getUserLoginId());
        input.setStatusId(ProgrammingContestUserRegistration.REGISTATION_STATUS_APPROVED);

        //ProgrammingContestUserRegistration programmingContestUserRegistration = programmingContestUserRegistrationService.save(input);
        ProgrammingContestUserRegistration programmingContestUserRegistration = programmingContestUserRegistrationService
            .updateStatus(input);
        return ResponseEntity.ok().body(programmingContestUserRegistration);
    }


    @GetMapping("get-all-programming-contest-user-registration-list")
    public ResponseEntity<?> getAllProgrammingContestUserRegistrationList(Principal principal) {
        List<ProgrammingContestUserRegistration> programmingContestUserRegistrationList = programmingContestUserRegistrationService
            .findAll();
        return ResponseEntity.ok().body(programmingContestUserRegistrationList);
    }

    @GetMapping("get-all-programming-contest-user-registration-approved-list/{contestId}")
    public ResponseEntity<?> getAllProgrammingContestUserRegistrationApprovedList(
        Principal principal,
        @PathVariable String contestId
    ) {
        //List<ProgrammingContestUserRegistration> programmingContestUserRegistrationList = programmingContestUserRegistrationService.findAll();

        List<ProgrammingContestUserRegistration> programmingContestUserRegistrations = programmingContestUserRegistrationService
            .findByContestIdAndStatusId(contestId, ProgrammingContestUserRegistration.REGISTATION_STATUS_APPROVED);

        return ResponseEntity.ok().body(programmingContestUserRegistrations);
    }

    @GetMapping("get-approved-programming-contests-of-user")
    public ResponseEntity<?> getApprovedProgrammingContestsOfUser(Principal principal) {
        log.info("getApprovedProgrammingContestsOfUser, userloginid = " + principal.getName());
        List<ProgrammingContestUserRegistration> programmingContestUserRegistrationList = programmingContestUserRegistrationService
            .findByUserLoginIdAndStatusId(principal.getName(),
                                          ProgrammingContestUserRegistration.REGISTATION_STATUS_APPROVED);
        return ResponseEntity.ok().body(programmingContestUserRegistrationList);
    }
}
