package com.hust.baseweb.applications.education.controller;

import com.hust.baseweb.applications.education.entity.QuizChoiceAnswer;
import com.hust.baseweb.applications.education.entity.QuizCourseTopic;
import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.model.quiz.QuizChoiceAnswerCreateInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizChooseAnswerInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionCreateInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
import com.hust.baseweb.applications.education.service.QuizChoiceAnswerService;
import com.hust.baseweb.applications.education.service.QuizCourseTopicService;
import com.hust.baseweb.applications.education.service.QuizQuestionService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Controller
@Validated
//@RequestMapping("/edu/class")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuizController {
    private QuizQuestionService quizQuestionService;
    private QuizChoiceAnswerService quizChoiceAnswerService;
    private QuizCourseTopicService quizCourseTopicService;
    private UserService userService;
    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-all-quiz-course-topics")
    public ResponseEntity<?> getAllQuizCourseTopics(Principal principal){
        log.info("getAllQuizCourseTopics");
        List<QuizCourseTopic> quizCourseTopics = quizCourseTopicService.findAll();
        return ResponseEntity.ok().body(quizCourseTopics);
    }
    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-quiz-levels")
    public ResponseEntity<?> getQuizLevelList(Principal principal){
        List<String> levels = new ArrayList<>();
        levels.add(QuizQuestion.QUIZ_LEVEL_EASY);
        levels.add(QuizQuestion.QUIZ_LEVEL_INTERMEDIATE);
        levels.add(QuizQuestion.QUIZ_LEVEL_HARD);
        return ResponseEntity.ok().body(levels);
    }
    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-yes-no-list")
    public ResponseEntity<?> getYesNoList(Principal principal){
        List<String> lst = new ArrayList<>();
        lst.add("Y");
        lst.add("N");
        return ResponseEntity.ok().body(lst);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/create-quiz-question")
    public ResponseEntity<?> createQuizQuestion(Principal principal, @RequestBody QuizQuestionCreateInputModel input){
        log.info("createQuizQuestion, topicId = " + input.getQuizCourseTopicId());
        QuizQuestion quizQuestion = quizQuestionService.save(input);
        return ResponseEntity.ok().body(quizQuestion);
    }
    @GetMapping("/get-all-quiz-questions")
    public ResponseEntity<?> getAllQuizQuestions(Principal principal){
        log.info("getAllQuizQuestions");
        List<QuizQuestion> quizQuestionList = quizQuestionService.findAll();
        return ResponseEntity.ok().body(quizQuestionList);
    }
    @GetMapping("/get-quiz-of-course/{courseId}")
    public ResponseEntity<?> getQuizOfCourse(Principal principal, @PathVariable String courseId){
        log.info("getQuizOfCourse, courseId = " + courseId);
        //List<QuizQuestion> quizQuestions = quizQuestionService.findQuizOfCourse(courseId);
        List<QuizQuestion> quizQuestions = quizQuestionService.findAll();
        List<QuizQuestionDetailModel> quizQuestionDetailModels = new ArrayList<>();
        for(QuizQuestion quizQuestion: quizQuestions){
            QuizQuestionDetailModel quizQuestionDetailModel = quizQuestionService.findQuizDetail(quizQuestion.getQuestionId());
            quizQuestionDetailModels.add(quizQuestionDetailModel);
        }
        return ResponseEntity.ok().body(quizQuestionDetailModels);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/create-quiz-choice-answer")
    public ResponseEntity<?> createQuizChoiceAnswer(Principal principal, @RequestBody QuizChoiceAnswerCreateInputModel input){
        log.info("createQuizChoiceAnswer, quizQuestionId = " + input.getQuizQuestionId() + " content = " + input.getChoiceAnswerContent());
        QuizChoiceAnswer quizChoiceAnswer = quizChoiceAnswerService.save(input);
        return ResponseEntity.ok().body(quizChoiceAnswer);
    }
    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-quiz-choice-answer-of-a-quiz/{quizQuestionId}")
    public ResponseEntity<?> getQuizChoiceAnswerOfAQuizQuestion(Principal principal, @PathVariable UUID quizQuestionId){
        log.info("getQuizChoiceAnswerOfAQuizQuestion, quizQuestionId = " + quizQuestionId);

        //List<QuizChoiceAnswer> quizChoiceAnswers = quizChoiceAnswerService.findAll();
        List<QuizChoiceAnswer> quizChoiceAnswers = quizChoiceAnswerService.findAllByQuizQuestionId(quizQuestionId);
        return ResponseEntity.ok().body(quizChoiceAnswers);
    }
    @GetMapping("/get-quiz-detail/{questionId}")
    public ResponseEntity<?> getQuizDetail(Principal principal, @PathVariable UUID questionId){
        QuizQuestionDetailModel quizQuestion = quizQuestionService.findQuizDetail(questionId);

        return ResponseEntity.ok().body(quizQuestion);
    }

    @PostMapping("/quiz-choose_answer")
    public ResponseEntity<?> quizChooseAnswer(Principal principal, QuizChooseAnswerInputModel input){
        UserLogin userLogin = userService.findById(principal.getName());
        log.info("quizChooseAnswer, userLoginId = " + userLogin.getUserLoginId());
        QuizQuestionDetailModel quizQuestionDetailModel = quizQuestionService.findQuizDetail(input.getQuizQuestionId());
        String ans = "FALSE";
        for(int i = 0; i < quizQuestionDetailModel.getQuizChoiceAnswerList().size(); i++){
            QuizChoiceAnswer quizChoiceAnswer = quizQuestionDetailModel.getQuizChoiceAnswerList().get(i);
            if(quizChoiceAnswer.getChoiceAnswerId().equals(input.getChoiceAnswerId()) && quizChoiceAnswer.getIsCorrectAnswer() == 'Y'){
                ans = "TRUE"; break;
            }
        }
        return ResponseEntity.ok().body(ans);
    }
}
