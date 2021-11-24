package com.hust.baseweb.applications.education.controller;

import com.google.gson.Gson;
import com.hust.baseweb.applications.education.classmanagement.service.ClassService;
import com.hust.baseweb.applications.education.entity.*;
import com.hust.baseweb.applications.education.model.GetClassDetailOM;
import com.hust.baseweb.applications.education.model.quiz.*;
import com.hust.baseweb.applications.education.service.CommentOnQuizQuestionService;
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
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

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

    private ClassService classService;

    private CommentOnQuizQuestionService commentOnQuizQuestionService;

    @PostMapping("/post-comment-on-quiz")
    public ResponseEntity<?> postCommentOnQuizQuestion(Principal principal,
                                                       @RequestBody CreateCommentOnQuizQuestionIM input){

        UserLogin u = userService.findById(principal.getName());
        log.info("postCommentOnQuizQuestion, user " + u.getUserLoginId() + " post comments = " + input.getComment());

        CommentOnQuizQuestion commentOnQuizQuestion = commentOnQuizQuestionService.createComment(input.getQuestionId(), input.getComment(), u);

        return ResponseEntity.ok().body(commentOnQuizQuestion);
    }
    @GetMapping("/get-list-comments-on-quiz/{questionId}")
    public ResponseEntity<?> getListCommentsOnQuiz(Principal principal, @PathVariable UUID questionId){
        List<CommentOnQuizQuestionDetailOM> lst = commentOnQuizQuestionService.findByQuestionId(questionId);
        return ResponseEntity.ok().body(lst);
    }
    @GetMapping("/get-number-comments-on-quiz/{questionId}")
    public ResponseEntity<?> getNumberCommentsOnQuiz(Principal principal, @PathVariable UUID questionId){
        int nbr = commentOnQuizQuestionService.getNumberCommentsOnQuiz(questionId);
        log.info("getNumberCommentsOnQuiz, questionId = " + questionId + " size = " + nbr);
        return ResponseEntity.ok().body(nbr);
    }


    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-all-quiz-course-topics")
    public ResponseEntity<?> getAllQuizCourseTopics(Principal principal) {
        log.info("getAllQuizCourseTopics");
        List<QuizCourseTopic> quizCourseTopics = quizCourseTopicService.findAll();
        return ResponseEntity.ok().body(quizCourseTopics);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/edu/teacher/course/quiz/detail/{questionId}")
    public ResponseEntity<?> getAllQuizCourseTopics(Principal principal, @PathVariable UUID questionId) {
        log.info("getQuizQuestion" + questionId);
        QuizQuestionDetailModel quizQuestionDetailModel = quizQuestionService.findById(questionId);
        return ResponseEntity.ok().body(quizQuestionDetailModel);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/update-quiz-question/{questionId}")
    public ResponseEntity<?> updateQuizQuestion(
        Principal principal,
        //@RequestBody QuizQuestionCreateInputModel input,
        @PathVariable UUID questionId,
        @RequestParam("QuizQuestionUpdateInputModel") String json,
        @RequestParam("files") MultipartFile[] files
    ) {

//        Gson g = new Gson();
//        QuizQuestionUpdateInputModel input = g.fromJson(json, QuizQuestionUpdateInputModel.class);
//        log.info("updateQuizQuestion, topicId = " + input.getQuizCourseTopicId());
        QuizQuestion quizQuestion = quizQuestionService.update(questionId, json, files);
        return ResponseEntity.ok().body(quizQuestion);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-quiz-course-topics-of-course/{courseId}")
    public ResponseEntity<?> getQuizCourseTopicsOfCourse(Principal principal, @PathVariable String courseId) {
        log.info("getQuizCourseTopicsOfCourse, courseId = " + courseId);
        //List<QuizCourseTopic> quizCourseTopics = quizCourseTopicService.findByEduCourse_Id(courseId);
        List<QuizCourseTopicDetailOM> quizCourseTopics = quizCourseTopicService.findTopicByCourseId(courseId);
        return ResponseEntity.ok().body(quizCourseTopics);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/create-quiz-course-topic")
    public ResponseEntity<?> createQuizCourseTopic(
        Principal principal, @RequestBody
        QuizCourseTopicCreateInputModel input
    ) {
        log.info("createQuizCourseTopic, topicId = " + input.getQuizCourseTopicId());
        QuizCourseTopic quizCourseTopic = quizCourseTopicService.save(input);
        return ResponseEntity.ok().body(quizCourseTopic);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-quiz-levels")
    public ResponseEntity<?> getQuizLevelList(Principal principal) {
        List<String> levels = new ArrayList<>();
        levels.add(QuizQuestion.QUIZ_LEVEL_EASY);
        levels.add(QuizQuestion.QUIZ_LEVEL_INTERMEDIATE);
        levels.add(QuizQuestion.QUIZ_LEVEL_HARD);
        return ResponseEntity.ok().body(levels);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-yes-no-list")
    public ResponseEntity<?> getYesNoList(Principal principal) {
        List<String> lst = new ArrayList<>();
        lst.add("Y");
        lst.add("N");
        return ResponseEntity.ok().body(lst);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/create-quiz-question")
    public ResponseEntity<?> createQuizQuestion(
        Principal principal,
        //@RequestBody QuizQuestionCreateInputModel input,
        @RequestParam("QuizQuestionCreateInputModel") String json,
        @RequestParam("files") MultipartFile[] files
    ) {
        UserLogin u = userService.findById(principal.getName());

//        Gson g = new Gson();
//        QuizQuestionCreateInputModel input = g.fromJson(json, QuizQuestionCreateInputModel.class);

        //System.out.println("hehehehehehehe");
//        log.info("createQuizQuestion, topicId = " + input.getQuizCourseTopicId());
        QuizQuestion quizQuestion = quizQuestionService.save(u, json, files);
        return ResponseEntity.ok().body(quizQuestion);
    }

    @GetMapping("/get-all-quiz-questions")
    public ResponseEntity<?> getAllQuizQuestions(Principal principal) {
        log.info("getAllQuizQuestions");
        List<QuizQuestion> quizQuestionList = quizQuestionService.findAll();
        return ResponseEntity.ok().body(quizQuestionList);
    }

    @GetMapping("/change-quiz-open-close-status/{questionId}")
    public ResponseEntity<?> changeQuizOpenCloseStatus(Principal principal, @PathVariable UUID questionId) {
        UserLogin u = userService.findById(principal.getName());
        QuizQuestion quizQuestion = quizQuestionService.changeOpenCloseStatus(u, questionId);
        return ResponseEntity.ok().body(quizQuestion);
    }

    @GetMapping("/get-quiz-of-class/{classId}")
    public ResponseEntity<?> getQuizOfClass(Principal principal, @PathVariable UUID classId) {
        GetClassDetailOM eduClass = classService.getClassDetail(classId);
        String courseId = eduClass.getCourseId();
        log.info("getQuizOfClass, classId = " + classId + ", courseId = " + courseId);
        List<QuizQuestion> quizQuestions = quizQuestionService.findQuizOfCourse(courseId);
        List<QuizQuestionDetailModel> quizQuestionDetailModels = new ArrayList<>();
        for (QuizQuestion q : quizQuestions) {
            QuizQuestionDetailModel quizQuestionDetailModel = quizQuestionService.findQuizDetail(q.getQuestionId());
            quizQuestionDetailModels.add(quizQuestionDetailModel);
        }
        return ResponseEntity.ok().body(quizQuestionDetailModels);
    }
    @GetMapping("/get-quiz-question-detail/{questionId}")
    public ResponseEntity<?> getQuizQuestionDetail(Principal principal, @PathVariable UUID questionId){
        //QuizQuestion q = quizQuestionService.findById(questionId);
        log.info("getQuizQuestionDetail, questionId = " + questionId);
        QuizQuestionDetailModel quizQuestionDetailModel = quizQuestionService.findQuizDetail(questionId);
        return ResponseEntity.ok().body(quizQuestionDetailModel);

    }
    @GetMapping("/get-published-quiz-of-class/{classId}")
    public ResponseEntity<?> getPublishedQuizOfClass(Principal principal, @PathVariable UUID classId) {
        GetClassDetailOM eduClass = classService.getClassDetail(classId);
        String courseId = eduClass.getCourseId();

//        log.info("getPublishedQuizOfClass, classId = " + classId + ", courseId = " + courseId);

        List<QuizQuestion> quizQuestions = quizQuestionService.findQuizOfCourse(courseId);
        List<QuizQuestionDetailModel> quizQuestionDetailModels = new ArrayList<>();

        for (QuizQuestion q : quizQuestions) {
            if (q.getStatusId().equals(QuizQuestion.STATUS_PRIVATE)) {
                continue;
            }
            QuizQuestionDetailModel quizQuestionDetailModel = quizQuestionService.findQuizDetail(q.getQuestionId());
            quizQuestionDetailModels.add(quizQuestionDetailModel);
        }

//        log.info("getPublishedQuizOfClass, classId = " + classId + ", courseId = " + courseId
//                 + " RETURN list.sz = " + quizQuestionDetailModels.size());

        return ResponseEntity.ok().body(quizQuestionDetailModels);
    }

    @GetMapping("/get-unpublished-quiz-of-course/{courseId}")
    public ResponseEntity<?> getUnPublishedQuizOfCourse(Principal principal, @PathVariable String courseId) {

        log.info("getUnPublishedQuizOfCourse, courseId = " + courseId);
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
                if (c1 == 0) {
                    return level1.compareTo(level2);
                } else {
                    return c1;
                }
            }
        });
        log.info("getUnPublishedQuizOfCourse, courseId = " + courseId
                 + " RETURN list.sz = " + quizQuestionDetailModels.size());

        return ResponseEntity.ok().body(quizQuestionDetailModels);
    }

    @GetMapping("/get-course-of-quiz-question/{questionId}")
    public ResponseEntity<?> getCourseOfQuizQuestion(Principal principal, @PathVariable UUID questionId) {
        log.info("getCourseOfQuizQuestion, questionId = " + questionId);
        EduCourse eduCourse = quizQuestionService.findCourseOfQuizQuestion(questionId);
        log.info("getCourseOfQuizQuestion, questionId = " + questionId + " got courseId = " + eduCourse.getId());
        return ResponseEntity.ok().body(eduCourse);
    }
    @GetMapping("/get-quiz-of-course-topic/{quizCourseTopicId}")
    public ResponseEntity<?> getQuizOfCourseTopic(@PathVariable String quizCourseTopicId){
        log.info("getQuizOfCourseTopic, quizCourseTopicId = " + quizCourseTopicId);
        List<QuizQuestion> quizQuestions = quizQuestionService.findQuizOfCourseTopic(quizCourseTopicId);

        List<QuizQuestionDetailModel> quizQuestionDetailModels = new ArrayList<>();
        for (QuizQuestion quizQuestion : quizQuestions) {
            QuizQuestionDetailModel quizQuestionDetailModel = quizQuestionService.findQuizDetail(quizQuestion.getQuestionId());
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
                if (c1 == 0) {
                    return level1.compareTo(level2);
                } else {
                    return c1;
                }
            }
        });
        return ResponseEntity.ok().body(quizQuestionDetailModels);
    }
    @GetMapping("/get-quiz-of-course/{courseId}")
    public ResponseEntity<?> getQuizOfCourse(Principal principal, @PathVariable String courseId) {
        log.info("getQuizOfCourse, courseId = " + courseId);
        List<QuizQuestion> quizQuestions = quizQuestionService.findQuizOfCourse(courseId);
        //List<QuizQuestion> quizQuestions = quizQuestionService.findAll();
        List<QuizQuestionDetailModel> quizQuestionDetailModels = new ArrayList<>();
        for (QuizQuestion quizQuestion : quizQuestions) {
            QuizQuestionDetailModel quizQuestionDetailModel = quizQuestionService.findQuizDetail(quizQuestion.getQuestionId());
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
                if (c1 == 0) {
                    return level1.compareTo(level2);
                } else {
                    return c1;
                }
            }
        });
        return ResponseEntity.ok().body(quizQuestionDetailModels);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/create-quiz-choice-answer")
    public ResponseEntity<?> createQuizChoiceAnswer(
        Principal principal,
        @RequestBody QuizChoiceAnswerCreateInputModel input
    ) {
        log.info("createQuizChoiceAnswer, quizQuestionId = " +
                 input.getQuizQuestionId() +
                 " content = " +
                 input.getChoiceAnswerContent());
        QuizChoiceAnswer quizChoiceAnswer = quizChoiceAnswerService.save(input);
        return ResponseEntity.ok().body(quizChoiceAnswer);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/update-quiz-choice-answer/{choiceAnswerId}")
    public ResponseEntity<?> createQuizChoiceAnswer(
        Principal principal,
        @PathVariable UUID choiceAnswerId,
        @RequestBody QuizChoiceAnswerCreateInputModel input
    ) {
        log.info("updateQuizChoiceAnswer " + choiceAnswerId);
        QuizChoiceAnswer quizChoiceAnswer = quizChoiceAnswerService.update(choiceAnswerId, input);
        return ResponseEntity.ok().body(quizChoiceAnswer);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @DeleteMapping("/delete-quiz-choice-answer/{choiceAnswerId}")
    public ResponseEntity<?> deleteQuizChoiceAnswer(
        Principal principal,
        @PathVariable UUID choiceAnswerId
    ) {
        log.info("deleteQuizChoiceAnswer " + choiceAnswerId);
        QuizChoiceAnswer quizChoiceAnswer = quizChoiceAnswerService.delete(choiceAnswerId);
        return ResponseEntity.ok().body(quizChoiceAnswer);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-quiz-choice-answer-detail/{choiceAnswerId}")
    public ResponseEntity<?> createQuizChoiceAnswer(
        Principal principal,
        @PathVariable UUID choiceAnswerId
    ) {
        log.info("get quiz choice answer detail " + choiceAnswerId);
        QuizChoiceAnswer quizChoiceAnswer = quizChoiceAnswerService.findById(choiceAnswerId);
        return ResponseEntity.ok().body(quizChoiceAnswer);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-quiz-choice-answer-of-a-quiz/{quizQuestionId}")
    public ResponseEntity<?> getQuizChoiceAnswerOfAQuizQuestion(
        Principal principal,
        @PathVariable UUID quizQuestionId
    ) {
        log.info("getQuizChoiceAnswerOfAQuizQuestion, quizQuestionId = " + quizQuestionId);

        //List<QuizChoiceAnswer> quizChoiceAnswers = quizChoiceAnswerService.findAll();
        List<QuizChoiceAnswer> quizChoiceAnswers = quizChoiceAnswerService.findAllByQuizQuestionId(quizQuestionId);
        return ResponseEntity.ok().body(quizChoiceAnswers);
    }

    @GetMapping("/get-quiz-detail/{questionId}")
    public ResponseEntity<?> getQuizDetail(Principal principal, @PathVariable UUID questionId) {
        QuizQuestionDetailModel quizQuestion = quizQuestionService.findQuizDetail(questionId);

        return ResponseEntity.ok().body(quizQuestion);
    }

    @PostMapping("/quiz-choose_answer")
    public ResponseEntity<?> quizChooseAnswer(
        @CurrentSecurityContext(expression = "authentication.name") String userId,
        @RequestBody @Valid QuizChooseAnswerInputModel input
    ) {
//        log.info("quizChooseAnswer, userLoginId = " + userLogin.getUserLoginId());

        boolean ans = quizQuestionService.checkAnswer(userId, input);

        return ResponseEntity.ok().body(ans);
    }

    @PostMapping("/remove-choice-answer-of-quiz")
    public ResponseEntity<?> removeChoiceAnswerOfQuiz(
        Principal principal,
        @RequestBody RemoveChoiceAnswerInputModel input
    ) {
        QuizChoiceAnswer quizChoiceAnswer = quizChoiceAnswerService.delete(input.getChoiceAnswerId());
        return ResponseEntity.ok().body(quizChoiceAnswer);
    }
}
