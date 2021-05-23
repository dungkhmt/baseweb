package com.hust.baseweb.applications.education.quiztest.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTest;
import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroup;
import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizParticipant;
import com.hust.baseweb.applications.education.quiztest.model.EditQuizTestInputModel;
import com.hust.baseweb.applications.education.quiztest.model.EduQuizTestModel;
import com.hust.baseweb.applications.education.quiztest.model.QuizTestCreateInputModel;
import com.hust.baseweb.applications.education.quiztest.model.StudentInTestQueryReturnModel;
import com.hust.baseweb.applications.education.quiztest.model.edutestquizparticipation.GetQuizTestParticipationExecutionResultInputModel;
import com.hust.baseweb.applications.education.quiztest.model.edutestquizparticipation.QuizTestParticipationExecutionResultOutputModel;
import com.hust.baseweb.applications.education.quiztest.model.quitestgroupquestion.AutoAssignQuestion2QuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.AutoAssignParticipants2QuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.GenerateQuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.repo.EduTestQuizParticipantRepo;
import com.hust.baseweb.applications.education.quiztest.service.EduQuizTestGroupService;
import com.hust.baseweb.applications.education.quiztest.service.EduTestQuizParticipantService;
import com.hust.baseweb.applications.education.quiztest.service.QuizTestService;
import com.hust.baseweb.service.UserService;
import com.hust.baseweb.entity.UserLogin;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Log4j2
@Controller
@Validated
@AllArgsConstructor(onConstructor = @__(@Autowired))
@CrossOrigin
public class QuizTestController {
    private QuizTestService quizTestService;
    private UserService userService;
    private EduTestQuizParticipantRepo eduTestQuizParticipationRepo;

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/create-quiz-test")
    public ResponseEntity<?> createQuizCourseTopic(
        Principal principal, 
        @RequestParam(required=false, name="QuizTestCreateInputModel") String json
    ) {
        Gson g =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        QuizTestCreateInputModel input = g.fromJson(json, QuizTestCreateInputModel.class);

        UserLogin user = userService.findById(principal.getName());

        System.out.println(input);
        return ResponseEntity.ok().body(quizTestService.save(input, user));
    }
    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/update-quiz-test")
    public ResponseEntity<?> updateQuizTest(Principal principal, @RequestBody EditQuizTestInputModel input){
        EduQuizTest eduQuizTest = quizTestService.update(input);
        return ResponseEntity.ok().body(eduQuizTest);
    }


    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-all-quiz-test-by-user")
    public ResponseEntity<?> getAllQuizTestByUserLogin(
        Principal principal
    ) {
        return ResponseEntity.ok().body(quizTestService.getAllTestByCreateUser(principal.getName()));
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-quiz-test")
    public ResponseEntity<?> getQuizTestByTestId(
        Principal principal,
        @RequestParam(required = false, name = "testId") String testId
    ) {
        return ResponseEntity.ok().body(quizTestService.getQuizTestById(testId));
    }

    @GetMapping("/get-all-quiz-test-user")
    public ResponseEntity<?> getAllQuizTestByUser(
        Principal principal
    ) {
        UserLogin user = userService.findById(principal.getName());
        List<EduQuizTestModel> listQuizTest = quizTestService.getListQuizByUserId(user.getUserLoginId());
        return ResponseEntity.ok().body(listQuizTest);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-all-student-in-test")
    public ResponseEntity<?> getAllStudentInTest(
        Principal principal, @RequestParam(required = false, name = "testId") String testId
    ) {
        testId = testId.replaceAll("\'", "");
        /* System.out.println("============================================================================================================");
        System.out.println(testId); */
        List<StudentInTestQueryReturnModel> list = quizTestService.getAllStudentInTest(testId);
        /* for (StudentInTestQueryReturnModel studentInTestQueryReturn : list) {
            System.out.println(studentInTestQueryReturn);
        } */
        if(list.isEmpty()) return ResponseEntity.ok().body("Error");
        return ResponseEntity.ok().body(list);

    }

    @PostMapping("/auto-assign-participants-2-quiz-test-group")
    public ResponseEntity<?> autoAssignParticipants2QuizTestGroup(Principal principal, @RequestBody
        AutoAssignParticipants2QuizTestGroupInputModel input
    ){
        boolean ok = quizTestService.autoAssignParticipants2QuizTestGroup(input);

        return ResponseEntity.ok().body(ok);
    }

    @PostMapping("auto-assign-question-2-quiz-group")
    public ResponseEntity<?> autoAssignQuestion2QuizTestGroup(Principal principal, @RequestBody
        AutoAssignQuestion2QuizTestGroupInputModel input) {

        boolean ok = quizTestService.autoAssignQuestion2QuizTestGroup(input);

        return ResponseEntity.ok().body(ok);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/reject-students-in-test")
    public ResponseEntity<?> rejectStudentInTest(
        Principal principal, 
        @RequestParam(required=false, name="testId") String testId,
        @RequestParam(required=false, name="studentList") String studentList
    ) {
        String[] students = studentList.split(";");
        return ResponseEntity.ok().body(quizTestService.rejectStudentsInTest(testId, students));
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/accept-students-in-test")
    public ResponseEntity<?> acceptStudentInTest(
        Principal principal, 
        @RequestParam(required=false, name="testId") String testId,
        @RequestParam(required=false, name="studentList") String studentList
    ) {
        String[] students = studentList.split(";");
        return ResponseEntity.ok().body(quizTestService.acceptStudentsInTest(testId, students));
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-test-groups-info")
    public ResponseEntity<?> getTestGroups(
        Principal principal, 
        @RequestParam(required=false, name="testId") String testId
    ) {
        return ResponseEntity.ok().body(quizTestService.getQuizTestGroupsInfoByTestId(testId));
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/delete-quiz-test-groups")
    public ResponseEntity<?> deleteQuizTestGroups(
        Principal principal, 
        @RequestParam(required=false, name="testId") String testId,
        @RequestParam(required=false, name="quizTestGroupList") String quizTestGroupList
    ) {
        String[] list = quizTestGroupList.split(";");
        return ResponseEntity.ok().body(quizTestService.deleteQuizTestGroups(testId, list));
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/get-quiz-test-participation-execution-result")
    public ResponseEntity<?> getQuizTestParticipationExecutionResult(Principal principal, @RequestBody
                                                                     GetQuizTestParticipationExecutionResultInputModel input
                                                                     ){
        List<QuizTestParticipationExecutionResultOutputModel> quizTestParticipationExecutionResultOutputModels =
            quizTestService.getQuizTestParticipationExecutionResult(input.getTestId());

        return ResponseEntity.ok().body(quizTestParticipationExecutionResultOutputModels);
    }

}
