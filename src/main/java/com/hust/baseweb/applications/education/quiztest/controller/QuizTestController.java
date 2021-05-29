package com.hust.baseweb.applications.education.quiztest.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hust.baseweb.applications.education.classmanagement.service.ClassService;
import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
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
import com.hust.baseweb.applications.education.service.QuizQuestionService;
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
import java.util.*;

@Log4j2
@Controller
@Validated
@AllArgsConstructor(onConstructor = @__(@Autowired))
@CrossOrigin
public class QuizTestController {
    private QuizTestService quizTestService;
    private UserService userService;
    private EduTestQuizParticipantRepo eduTestQuizParticipationRepo;
    private QuizQuestionService quizQuestionService;
    private ClassService classService;
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
    @GetMapping("/get-list-quiz-for-assignment-of-test/{testId}")
    public ResponseEntity<?> getListQuizForAssignmentOfTest(Principal principal, @PathVariable String testId){
        EduQuizTest eduQuizTest = quizTestService.getQuizTestById(testId);
        if(eduQuizTest == null){
            return ResponseEntity.ok().body(new ArrayList());
        }
        UUID classId = eduQuizTest.getClassId();
        EduClass eduClass = classService.findById(classId);

        String courseId = null;
        if(eduClass != null) courseId = eduClass.getEduCourse().getId();

        log.info("getListQuizForAssignmentOfTest, testId = " + testId + " courseId = " + courseId);
        List<QuizQuestion> quizQuestions = quizQuestionService.findQuizOfCourse(courseId);
        List<QuizQuestionDetailModel> quizQuestionDetailModels = new ArrayList<>();
        for (QuizQuestion q : quizQuestions) {
            if (q.getStatusId().equals(QuizQuestion.STATUS_PUBLIC)) {
                continue;
            }
            QuizQuestionDetailModel quizQuestionDetailModel = quizQuestionService.findQuizDetail(q.getQuestionId());
            quizQuestionDetailModels.add(quizQuestionDetailModel);
        }
        Collections.sort(quizQuestionDetailModels, new Comparator<QuizQuestionDetailModel>() {
            @Override
            public int compare(QuizQuestionDetailModel o1, QuizQuestionDetailModel o2) {
                String topic1 = o1.getQuizCourseTopic().getQuizCourseTopicId();
                String topic2 = o2.getQuizCourseTopic().getQuizCourseTopicId();
                String level1 = o1.getLevelId();
                String level2 = o2.getLevelId();
                int c1 = topic1.compareTo(topic2);
                if(c1 == 0) return level1.compareTo(level2);
                else return c1;
            }
        });
        log.info("getListQuizForAssignmentOfTest, testId = " + testId + " courseId = " + courseId
                 + " RETURN list.sz = " + quizQuestionDetailModels.size());

        return ResponseEntity.ok().body(quizQuestionDetailModels);

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
