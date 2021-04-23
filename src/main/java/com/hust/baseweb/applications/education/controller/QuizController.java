package com.hust.baseweb.applications.education.controller;

import com.hust.baseweb.applications.education.entity.QuizChoiceAnswer;
import com.hust.baseweb.applications.education.entity.QuizCourseTopic;
import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.model.quiz.QuizChoiceAnswerCreateInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizCourseTopicCreateInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionCreateInputModel;
import com.hust.baseweb.applications.education.service.QuizChoiceAnswerService;
import com.hust.baseweb.applications.education.service.QuizCourseTopicService;
import com.hust.baseweb.applications.education.service.QuizQuestionService;
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

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-all-quiz-course-topics")
    public ResponseEntity<?> getAllQuizCourseTopics(Principal principal){
        log.info("getAllQuizCourseTopics");
        List<QuizCourseTopic> quizCourseTopics = quizCourseTopicService.findAll();
        return ResponseEntity.ok().body(quizCourseTopics);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-quiz-course-topics-of-course/{courseId}")
    public ResponseEntity<?> getQuizCourseTopicsOfCourse(Principal principal, @PathVariable String courseId){
        log.info("getQuizCourseTopicsOfCourse, courseId = " + courseId);
        List<QuizCourseTopic> quizCourseTopics = quizCourseTopicService.findByEduCourse_Id(courseId);
        return ResponseEntity.ok().body(quizCourseTopics);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/create-quiz-course-topic")
    public ResponseEntity<?> createQuizCourseTopic(Principal principal, @RequestBody
        QuizCourseTopicCreateInputModel input){
        log.info("createQuizCourseTopic, topicId = " + input.getQuizCourseTopicId());
        QuizCourseTopic quizCourseTopic = quizCourseTopicService.save(input);
        return ResponseEntity.ok().body(quizCourseTopic);
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
}
