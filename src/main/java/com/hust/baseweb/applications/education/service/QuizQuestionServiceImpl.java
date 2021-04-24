package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.LogUserLoginQuizQuestion;
import com.hust.baseweb.applications.education.entity.QuizChoiceAnswer;
import com.hust.baseweb.applications.education.entity.QuizCourseTopic;
import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.model.quiz.QuizChooseAnswerInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionCreateInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
import com.hust.baseweb.applications.education.repo.LogUserLoginQuizQuestionRepo;
import com.hust.baseweb.applications.education.repo.QuizChoiceAnswerRepo;
import com.hust.baseweb.applications.education.repo.QuizCourseTopicRepo;
import com.hust.baseweb.applications.education.repo.QuizQuestionRepo;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuizQuestionServiceImpl implements QuizQuestionService {
    private QuizQuestionRepo quizQuestionRepo;
    private QuizCourseTopicRepo quizCourseTopicRepo;
    private QuizCourseTopicService quizCourseTopicService;
    private QuizChoiceAnswerRepo quizChoiceAnswerRepo;
    private LogUserLoginQuizQuestionRepo logUserLoginQuizQuestionRepo;

    @Override
    public QuizQuestion save(QuizQuestionCreateInputModel input) {
        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setLevelId(input.getLevelId());
        quizQuestion.setQuestionContent(input.getQuestionContent());
        quizQuestion.setStatusId(QuizQuestion.STATUS_PRIVATE);

        QuizCourseTopic quizCourseTopic = quizCourseTopicRepo.findById(input.getQuizCourseTopicId()).orElse(null);

        quizQuestion.setQuizCourseTopic(quizCourseTopic);

        quizQuestion = quizQuestionRepo.save(quizQuestion);

        return quizQuestion;
    }

    @Override
    public List<QuizQuestion> findAll() {
        return quizQuestionRepo.findAll();
    }

    @Override
    public List<QuizQuestion> findQuizOfCourse(String courseId) {
        List<QuizCourseTopic> quizCourseTopics = quizCourseTopicService.findAllByEduCourse(courseId);
        //List<String> quizCourseTopicIds = quizCourseTopics.stream()
        //                                                  .map(quizCourseTopic -> quizCourseTopic.getQuizCourseTopicId()).collect(
        //    Collectors.toList());
        for(QuizCourseTopic q: quizCourseTopics){
            log.info("findQuizOfCourse, courseId = " + courseId + ", topic = " + q.getQuizCourseTopicId() + ", " + q.getQuizCourseTopicName() + ", courseId = " + q.getEduCourse().getId());

        }
        List<QuizQuestion> quizQuestions = quizQuestionRepo.findAllByQuizCourseTopicIn(quizCourseTopics);
        log.info("findQuizOfCourse, courseId = " + courseId + ", quizCourseTopics.sz = " + quizCourseTopics.size()
                 + " return quizQuestions.sz = " + quizQuestions.size());
        return quizQuestions;
    }

    @Override
    public QuizQuestionDetailModel findQuizDetail(UUID questionId) {
        QuizQuestion quizQuestion  = quizQuestionRepo.findById(questionId).orElse(null);
        QuizQuestionDetailModel quizQuestionDetailModel = new QuizQuestionDetailModel();
        quizQuestionDetailModel.setLevelId(quizQuestion.getLevelId());
        quizQuestionDetailModel.setStatement(quizQuestion.getQuestionContent());
        quizQuestionDetailModel.setQuizCourseTopic(quizQuestion.getQuizCourseTopic());
        quizQuestionDetailModel.setQuestionId(quizQuestion.getQuestionId());
        quizQuestionDetailModel.setStatusId(quizQuestion.getStatusId());
        List<QuizChoiceAnswer> quizChoiceAnswers = quizChoiceAnswerRepo.findAllByQuizQuestion(quizQuestion);
        log.info("findQuizDetail, questionId = " + questionId + ", GOT quizChoideAnswers.sz = " + quizChoiceAnswers.size());

        quizQuestionDetailModel.setQuizChoiceAnswerList(quizChoiceAnswers);

        return quizQuestionDetailModel;
    }

    @Override
    public QuizQuestion changeOpenCloseStatus(UUID questionId) {
        QuizQuestion quizQuestion = quizQuestionRepo.findById(questionId).orElse(null);
        if(quizQuestion.getStatusId().equals(QuizQuestion.STATUS_PRIVATE)){
            quizQuestion.setStatusId(QuizQuestion.STATUS_PUBLIC);
        }else{
            quizQuestion.setStatusId(QuizQuestion.STATUS_PRIVATE);
        }
        quizQuestion = quizQuestionRepo.save(quizQuestion);
        return quizQuestion;
    }

    @Override
    public boolean checkAnswer(UserLogin userLogin, QuizChooseAnswerInputModel quizChooseAnswerInputModel) {
        QuizQuestionDetailModel quizQuestionDetail = findQuizDetail(quizChooseAnswerInputModel.getQuestionId());

        LogUserLoginQuizQuestion logUserLoginQuizQuestion = new LogUserLoginQuizQuestion();
        logUserLoginQuizQuestion.setUserLoginId(userLogin.getUserLoginId());
        logUserLoginQuizQuestion.setQuestionId(quizChooseAnswerInputModel.getQuestionId());
        logUserLoginQuizQuestion.setCreateStamp(new Date());
        logUserLoginQuizQuestion = logUserLoginQuizQuestionRepo.save(logUserLoginQuizQuestion);

        boolean ans = true;

        List<UUID> correctAns = quizQuestionDetail
            .getQuizChoiceAnswerList()
            .stream()
            .filter(answer -> answer.getIsCorrectAnswer() == 'Y')
            .map(QuizChoiceAnswer::getChoiceAnswerId)
            .collect(Collectors.toList());

        if (!correctAns.containsAll(quizChooseAnswerInputModel.getChooseAnsIds())) {
            ans = false;
        }
        return ans;
    }
}
