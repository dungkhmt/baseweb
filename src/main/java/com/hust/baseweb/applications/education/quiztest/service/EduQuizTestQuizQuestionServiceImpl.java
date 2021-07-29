package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTestQuizQuestion;
import com.hust.baseweb.applications.education.quiztest.model.quiztestquestion.CreateQuizTestQuestionInputModel;
import com.hust.baseweb.applications.education.quiztest.repo.EduQuizTestQuizQuestionRepo;
import com.hust.baseweb.applications.education.repo.QuizQuestionRepo;
import com.hust.baseweb.applications.education.service.QuizQuestionService;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class EduQuizTestQuizQuestionServiceImpl implements EduQuizTestQuizQuestionService{
    private EduQuizTestQuizQuestionRepo eduQuizTestQuizQuestionRepo;
    private QuizQuestionRepo quizQuestionRepo;
    private QuizQuestionService quizQuestionService;

    @Override
    public EduQuizTestQuizQuestion createQuizTestQuestion(UserLogin u, CreateQuizTestQuestionInputModel input) {
        EduQuizTestQuizQuestion eduQuizTestQuizQuestion = eduQuizTestQuizQuestionRepo.findByTestIdAndQuestionId(input.getTestId(), input.getQuestionId());
        if(eduQuizTestQuizQuestion != null){
            log.info("createQuizTestQuestion, item (test " + input.getTestId() + ", question " + input.getQuestionId() + ") EXISTS");
            if(!eduQuizTestQuizQuestion.getStatusId().equals(EduQuizTestQuizQuestion.STATUS_CREATED)){
                eduQuizTestQuizQuestion.setStatusId(EduQuizTestQuizQuestion.STATUS_CREATED);
                eduQuizTestQuizQuestion = eduQuizTestQuizQuestionRepo.save(eduQuizTestQuizQuestion);
            }
            return eduQuizTestQuizQuestion;
        }
        eduQuizTestQuizQuestion = new EduQuizTestQuizQuestion();
        eduQuizTestQuizQuestion.setQuestionId(input.getQuestionId());
        eduQuizTestQuizQuestion.setTestId(input.getTestId());
        eduQuizTestQuizQuestion.setCreatedByUserLoginId(u.getUserLoginId());
        eduQuizTestQuizQuestion.setCreatedStamp(new Date());
        eduQuizTestQuizQuestion.setStatusId(EduQuizTestQuizQuestion.STATUS_CREATED);

        eduQuizTestQuizQuestion = eduQuizTestQuizQuestionRepo.save(eduQuizTestQuizQuestion);

        return eduQuizTestQuizQuestion;
    }

    @Override
    public EduQuizTestQuizQuestion removeQuizTestQuestion(UserLogin u, CreateQuizTestQuestionInputModel input) {
        EduQuizTestQuizQuestion eduQuizTestQuizQuestion = eduQuizTestQuizQuestionRepo
            .findByTestIdAndQuestionId(input.getTestId(),input.getQuestionId());

        if(eduQuizTestQuizQuestion != null){
            eduQuizTestQuizQuestion.setStatusId(EduQuizTestQuizQuestion.STATUS_CANCELLED);
            eduQuizTestQuizQuestion = eduQuizTestQuizQuestionRepo.save(eduQuizTestQuizQuestion);
        }
        return eduQuizTestQuizQuestion;
    }

    @Override
    public List<QuizQuestionDetailModel> findAllByTestId(String testId) {
        //List<EduQuizTestQuizQuestion> eduQuizTestQuizQuestions = eduQuizTestQuizQuestionRepo.findAllByTestId(testId);
        List<EduQuizTestQuizQuestion> eduQuizTestQuizQuestions = eduQuizTestQuizQuestionRepo
            .findAllByTestIdAndStatusId(testId, EduQuizTestQuizQuestion.STATUS_CREATED);


        List<UUID> questionIds = new ArrayList();
        for(EduQuizTestQuizQuestion q: eduQuizTestQuizQuestions){
            questionIds.add(q.getQuestionId());
        }
        List<QuizQuestion> quizQuestions = quizQuestionRepo.findAllByQuestionIdIn(questionIds);

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
        log.info("findAllByTestId, testId = " + testId
                 + " RETURN list.sz = " + quizQuestionDetailModels.size());

        return quizQuestionDetailModels;

    }
}
