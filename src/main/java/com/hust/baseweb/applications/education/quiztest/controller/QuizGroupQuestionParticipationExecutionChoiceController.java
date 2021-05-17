package com.hust.baseweb.applications.education.quiztest.controller;

import com.hust.baseweb.applications.education.model.quiz.QuizChooseAnswerInputModel;
import com.hust.baseweb.applications.education.service.QuizChoiceAnswerService;
import com.hust.baseweb.applications.education.service.QuizQuestionService;
import com.hust.baseweb.entity.UserLogin;
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

@Log4j2
@Controller
@Validated
@AllArgsConstructor(onConstructor = @__(@Autowired))
@CrossOrigin

public class QuizGroupQuestionParticipationExecutionChoiceController {
    private QuizQuestionService quizQuestionService;

    private QuizChoiceAnswerService quizChoiceAnswerService;

    private UserService userService;





    @PostMapping("/quiz-test-choose_answer-by-user")
    public ResponseEntity<?> quizChooseAnswer(
        Principal principal,
        @RequestBody @Valid QuizChooseAnswerInputModel input
    ) {
        UserLogin userLogin = userService.findById(principal.getName());
        log.info("quizChooseAnswer, userLoginId = " + userLogin.getUserLoginId());

        boolean ans = quizQuestionService.checkAnswer(userLogin, input);

        return ResponseEntity.ok().body(ans);
    }
}
