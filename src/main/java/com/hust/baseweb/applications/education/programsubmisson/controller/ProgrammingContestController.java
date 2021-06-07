package com.hust.baseweb.applications.education.programsubmisson.controller;

import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblem;
import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContest;
import com.hust.baseweb.applications.education.programsubmisson.model.DistributeProblemsOfContestToParticipantsInputModel;
import com.hust.baseweb.applications.education.programsubmisson.service.ProgrammingContestService;
import com.hust.baseweb.applications.education.programsubmisson.service.ProgrammingContestUserRegistrationService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor_ = @Autowired)
@Log4j2
public class ProgrammingContestController {

    private ProgrammingContestService programmingContestService;
    private UserService userService;
    private ProgrammingContestUserRegistrationService programmingContestUserRegistrationService;


    @PostMapping("distribute-problems-of-contest-to-participants")
    public ResponseEntity<?> distributeProblemsOfContestToParticipants(
        Principal principal, @RequestBody
        DistributeProblemsOfContestToParticipantsInputModel input
    ) {
        log.info("distributeProblemsOfContestToParticipants, contestId = " + input.getContestId());
        boolean ok = programmingContestService.distributeProblemsOfContestToParticipants(input.getContestId());

        return ResponseEntity.ok().body("OK");

    }

    @GetMapping("get-programming-contest-types")
    public ResponseEntity<?> getProgrammingContestTypes() {
        List<String> types = new ArrayList();
        types.add(ProgrammingContest.CONTEST_TYPE_PARTICIPANT_IDENTICAL);
        types.add(ProgrammingContest.CONTEST_TYPE_PARTICIPANT_SPECIFIC);
        return ResponseEntity.ok().body(types);
    }

    @GetMapping("get-programming-contest/{contestId}")
    public ResponseEntity<?> getProgrammingContest(Principal principal, @PathVariable String contestId) {
        ProgrammingContest programmingContest = programmingContestService.findByContestId(contestId);
        return ResponseEntity.ok().body(programmingContest);
    }

    @GetMapping("get-problems-of-programming-contest-and-user/{contestId}")
    public ResponseEntity<?> getProblemsOfProgrammingContestAndUser(
        Principal principal,
        @PathVariable String contestId
    ) {
        UserLogin userLogin = userService.findById(principal.getName());
        List<ContestProblem> contestProblems = programmingContestUserRegistrationService.getProblemsOfContestAndUser(
            contestId,
            userLogin.getUserLoginId());
        return ResponseEntity.ok().body(contestProblems);
    }

}
