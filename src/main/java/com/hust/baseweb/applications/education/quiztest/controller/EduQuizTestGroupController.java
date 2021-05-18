package com.hust.baseweb.applications.education.quiztest.controller;

import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTest;
import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroup;
import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizParticipant;
import com.hust.baseweb.applications.education.quiztest.model.QuizGroupTestDetailModel;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.GenerateQuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.repo.EduTestQuizParticipantRepo;
import com.hust.baseweb.applications.education.quiztest.service.EduQuizTestGroupService;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Controller
@Validated
@AllArgsConstructor(onConstructor = @__(@Autowired))
@CrossOrigin
public class EduQuizTestGroupController {

    private EduQuizTestGroupService eduQuizTestGroupService;
    private UserService userService;
    private EduTestQuizParticipantRepo eduTestQuizParticipationRepo;

    @PostMapping("/generate-quiz-test-group")
    public ResponseEntity<?> generateQuizTestGroup(Principal principal, @RequestBody
        GenerateQuizTestGroupInputModel input){
        List<EduTestQuizGroup> eduTestQuizGroups = eduQuizTestGroupService.generateQuizTestGroups(input);

        return ResponseEntity.ok().body(eduTestQuizGroups);
    }

    @GetMapping("/get-quiz-test-participation-group-question")
    public ResponseEntity<?> getTestGroupQuestionByUser(Principal principal, @RequestBody(required = true) String testID ) {

        System.out.println(testID);
        EduTestQuizParticipant testParticipant = eduTestQuizParticipationRepo.findEduTestQuizParticipantByParticipantUserLoginIdAndAndTestId(principal.getName(), testID);

        if( testParticipant == null || (!testParticipant.getStatusId().equals(EduTestQuizParticipant.STATUS_APPROVED)))
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.ok().body(eduQuizTestGroupService.getTestGroupQuestionDetail(principal, testID));
    }


}
