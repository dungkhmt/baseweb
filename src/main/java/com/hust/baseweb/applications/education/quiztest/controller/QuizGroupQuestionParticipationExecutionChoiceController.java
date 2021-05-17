package com.hust.baseweb.applications.education.quiztest.controller;

import com.hust.baseweb.applications.education.quiztest.entity.QuizGroupQuestionParticipationExecutionChoice;
import com.hust.baseweb.applications.education.quiztest.model.QuizGroupQuestionParticipationExecutionChoiceInputModel;
import com.hust.baseweb.applications.education.quiztest.repo.QuizGroupQuestionParticipationExecutionChoiceRepo;
import com.hust.baseweb.applications.education.service.QuizQuestionService;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Log4j2
@Controller
@Validated
@AllArgsConstructor(onConstructor = @__(@Autowired))
@CrossOrigin

public class QuizGroupQuestionParticipationExecutionChoiceController {

    QuizGroupQuestionParticipationExecutionChoiceRepo quizGroupQuestionParticipationExecutionChoiceRepo;



    @PostMapping("/quiz-test-choose_answer-by-user")
    public ResponseEntity<?> quizChooseAnswer(
        Principal principal,
        @RequestBody @Valid QuizGroupQuestionParticipationExecutionChoiceInputModel input
    ) {
        UUID questionId = input.getQuestionId();
        UUID groupId = input.getQuizGroupId();
        String userId = principal.getName();
        List<UUID> chooseAnsIds = input.getChooseAnsIds();
        for (UUID choiceId:
             chooseAnsIds) {
            QuizGroupQuestionParticipationExecutionChoice tmp = new QuizGroupQuestionParticipationExecutionChoice();
            tmp.setQuestionId(questionId);
            tmp.setQuizGroupId(groupId);
            tmp.setParticipationUserLoginId(userId);
            tmp.setChoiceAnswerId(choiceId);
            quizGroupQuestionParticipationExecutionChoiceRepo.save(tmp);
        }
        return ResponseEntity.ok().body(chooseAnsIds);
    }
}
