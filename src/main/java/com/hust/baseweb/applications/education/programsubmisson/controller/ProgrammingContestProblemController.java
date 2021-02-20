package com.hust.baseweb.applications.education.programsubmisson.controller;

import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblem;
import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContest;
import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContestProblem;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateProgrammingContestProblemInputModel;
import com.hust.baseweb.applications.education.programsubmisson.service.ContestProblemService;
import com.hust.baseweb.applications.education.programsubmisson.service.ProgrammingContestProblemService;
import com.hust.baseweb.applications.education.programsubmisson.service.ProgrammingContestService;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
@Log4j2
@AllArgsConstructor(onConstructor_ = @Autowired)
public class ProgrammingContestProblemController {
    private ProgrammingContestProblemService programmingContestProblemService;
    private UserService userService;
    private ContestProblemService contestProblemService;
    private ProgrammingContestService programmingContestService;

    @PostMapping("create-programmingcontest-problem")
    public ResponseEntity<?> createProgrammingContestProblem(Principal principal, @RequestBody
                                                             CreateProgrammingContestProblemInputModel input
                                                             ){
        log.info("createProgrammingContestProblem, contestId = " + input.getContestId() + " problemId = " + input.getProblemId());
        ProgrammingContestProblem programmingContestProblem = programmingContestProblemService.save(input);
        return ResponseEntity.ok().body(programmingContestProblem);
    }



    @GetMapping("get-all-programming-contest-problem-list")
    public ResponseEntity<?> getAllProgrammingContestProblemList(Principal principal){
        List<ProgrammingContestProblem> programmingContestProblemList = programmingContestProblemService.findAll();
        return ResponseEntity.ok().body(programmingContestProblemList);
    }
    @GetMapping("get-problems-of-programming-contest/{contestId}")
    public ResponseEntity<?> getProblemsOfProgrammingContest(Principal principal, @PathVariable String contestId){
        log.info("getProblemsOfProgrammingContest, contestId = " + contestId);
        List<ContestProblem> contestProblems = programmingContestService.getProblemsOfContest(contestId);
        /*
        List<ProgrammingContestProblem> programmingContestProblems = programmingContestProblemService.findByContestId(contestId);
        List<ContestProblem> contestProblems = new ArrayList();
        for(ProgrammingContestProblem programmingContestProblem: programmingContestProblems){
            String problemId = programmingContestProblem.getProblemId();
            ContestProblem contestProblem = contestProblemService.findByProblemId(problemId);
            contestProblems.add(contestProblem);
        }
        */
        return ResponseEntity.ok().body(contestProblems);
    }
}
