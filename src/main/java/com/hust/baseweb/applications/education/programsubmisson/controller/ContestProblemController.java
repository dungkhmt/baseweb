package com.hust.baseweb.applications.education.programsubmisson.controller;

import com.google.gson.Gson;
import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblem;
import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblemTest;
import com.hust.baseweb.applications.education.programsubmisson.model.ContestProblemInputModel;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateContestProblemTestInputModel;
import com.hust.baseweb.applications.education.programsubmisson.service.ContestProblemService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Controller
@AllArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin

public class ContestProblemController {

    private ContestProblemService contestProblemService;

    @GetMapping("get-contest-problem-level-list")
    public ResponseEntity<?> getContestProblemLevelList(Principal principal) {
        log.info("getContestProblemLevelList");
        List<String> levels = new ArrayList<>();
        levels.add(ContestProblem.CONTEST_PROBLEM_LEVEL_BEGINNER);
        levels.add(ContestProblem.CONTEST_PROBLEM_LEVEL_INTERMEDIATE);
        levels.add(ContestProblem.CONTEST_PROBLEM_LEVEL_ADVANCED);
        levels.add(ContestProblem.CONTEST_PROBLEM_LEVEL_PROFESSIONAL);
        return ResponseEntity.ok().body(levels);
    }

    @GetMapping("get-contest-problem-category-list")
    public ResponseEntity<?> getContestProblemCategoryList(Principal principal) {
        log.info("getContestProblemCategoryList");
        List<String> categories = new ArrayList<>();
        categories.add(ContestProblem.CONTEST_PROBLEM_CATEGORY_NAIVE);
        categories.add(ContestProblem.CONTEST_PROBLEM_CATEGORY_DIRECT);
        categories.add(ContestProblem.CONTEST_PROBLEM_CATEGORY_EXHAUSTIVE);
        categories.add(ContestProblem.CONTEST_PROBLEM_CATEGORY_GREEDY);
        categories.add(ContestProblem.CONTEST_PROBLEM_CATEGORY_DIVIDE_AND_CONQUER);
        categories.add(ContestProblem.CONTEST_PROBLEM_CATEGORY_DYNAMICPROGRAMMING);
        categories.add(ContestProblem.CONTEST_PROBLEM_CATEGORY_GRAPHS);
        categories.add(ContestProblem.CONTEST_PROBLEM_CATEGORY_DFS);
        categories.add(ContestProblem.CONTEST_PROBLEM_CATEGORY_BFS);

        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("get-contest-problem-test-list/{problemId}")
    public ResponseEntity<?> getContestProblemTestList(Principal principal, @PathVariable String problemId) {
        List<ContestProblemTest> contestProblemTests = contestProblemService.findAllContestProblemTestByProblemId(
            problemId);
        log.info("getContestProblemTestList, GOT " + contestProblemTests.size() + " items");
        return ResponseEntity.ok().body(contestProblemTests);
    }

    @PostMapping("/create-contest-problem-test")
    public ResponseEntity<?> createContestProblemTest(
        Principal principal, @RequestParam("inputJson") String inputJson,
        @RequestParam("files") MultipartFile[] files
    ) {
        Gson gson = new Gson();
        CreateContestProblemTestInputModel createContestProblemTestInputModel = gson.fromJson(
            inputJson,
            CreateContestProblemTestInputModel.class);
        //String rootDir = uploadConfigProperties.getRootPath() + uploadConfigProperties.getProgramSubmissionDataPath();
        //String problemDir = rootDir +
        log.info("createContestProblemTest, inputJson = " + inputJson);

        ContestProblemTest contestProblemTest = contestProblemService.createContestProblemTest(
            createContestProblemTestInputModel,
            files);

        return ResponseEntity.ok().body(contestProblemTest);
    }

    @PostMapping("/create-contest-problem")
    public ResponseEntity<?> createContestProblem(Principal principal, @RequestBody ContestProblemInputModel input) {
        ContestProblem contestProblem = contestProblemService.save(input);
        return ResponseEntity.ok().body(contestProblem);
    }

    @PostMapping("/update-contest-problem")
    public ResponseEntity<?> updateContestProblem(
        Principal principal, @RequestParam("inputJson") String inputJson,
        @RequestParam("files") MultipartFile[] files
    ) {
        Gson gson = new Gson();
        ContestProblemInputModel contestProblemInputModel = gson.fromJson(inputJson, ContestProblemInputModel.class);
        ContestProblem contestProblem = contestProblemService.update(contestProblemInputModel);
        return ResponseEntity.ok().body(contestProblem);
    }


    @GetMapping("get-contest-problem/{problemId}")
    public ResponseEntity<?> getContestProblem(Principal principal, @PathVariable String problemId) {
        log.info("getContestProblem, problemId = " + problemId);
        ContestProblem contestProblem = contestProblemService.findByProblemId(problemId);
        return ResponseEntity.ok().body(contestProblem);
    }

    @GetMapping("contest-problem-list")
    public ResponseEntity<?> getAllContestProblems(Principal principal) {
        log.info("getAllContestProblems....user = " + principal.getName());
        List<ContestProblem> contestProblems = contestProblemService.findAll();
        return ResponseEntity.ok().body(contestProblems);
    }

}
