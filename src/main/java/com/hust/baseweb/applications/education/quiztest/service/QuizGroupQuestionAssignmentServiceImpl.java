package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroup;
import com.hust.baseweb.applications.education.quiztest.entity.QuizGroupQuestionAssignment;
import com.hust.baseweb.applications.education.quiztest.model.quitestgroupquestion.QuizGroupQuestionDetailOutputModel;
import com.hust.baseweb.applications.education.quiztest.repo.EduQuizTestGroupRepo;
import com.hust.baseweb.applications.education.quiztest.repo.QuizGroupQuestionAssignmentRepo;
import com.hust.baseweb.applications.education.repo.QuizQuestionRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuizGroupQuestionAssignmentServiceImpl implements QuizGroupQuestionAssignmentService{
    private QuizGroupQuestionAssignmentRepo quizGroupQuestionAssignmentRepo;
    private EduQuizTestGroupRepo eduQuizTestGroupRepo;
    private QuizQuestionRepo quizQuestionRepo;

    @Override
    public List<QuizGroupQuestionDetailOutputModel> findAllQuizGroupQuestionAssignmentOfTest(String testId) {
        List<EduTestQuizGroup> eduQuizTestGroups = eduQuizTestGroupRepo.findByTestId(testId);
        // TO BE IMPROVED
        List<QuizQuestion> quizQuestions = quizQuestionRepo.findAll();
        HashMap<UUID, QuizQuestion> mId2QuizQuestion = new HashMap();
        for(QuizQuestion q: quizQuestions){
            mId2QuizQuestion.put(q.getQuestionId(),q);
        }

        HashMap<UUID, EduTestQuizGroup> mId2QuizTestGroup = new HashMap();
        for(EduTestQuizGroup g: eduQuizTestGroups){
            mId2QuizTestGroup.put(g.getQuizGroupId(),g);
        }

        log.info("findAllQuizGroupQuestionAssignmentOfTest, groups.sz = " + eduQuizTestGroups.size());
        List<UUID> quizGroupIds= eduQuizTestGroups.stream().map(eduQuizTestGroup -> eduQuizTestGroup.getQuizGroupId()).collect(
            Collectors.toList());
        for(UUID id: quizGroupIds){
            log.info("findAllQuizGroupQuestionAssignmentOfTest, groupId " + id);
        }
        List<QuizGroupQuestionAssignment> quizGroupQuestionAssignment = quizGroupQuestionAssignmentRepo.findAllByQuizGroupIdIn(quizGroupIds);
        log.info("findAllQuizGroupQuestionAssignmentOfTest, return sz = " + quizGroupQuestionAssignment.size());
        List<QuizGroupQuestionDetailOutputModel> quizGroupQuestionDetailOutputModels = new ArrayList();
        for(QuizGroupQuestionAssignment qgq: quizGroupQuestionAssignment){
            QuizQuestion q = mId2QuizQuestion.get(qgq.getQuestionId());
            EduTestQuizGroup g = mId2QuizTestGroup.get(qgq.getQuizGroupId());
            QuizGroupQuestionDetailOutputModel quizGroupQuestionDetailOutputModel  = new QuizGroupQuestionDetailOutputModel();
            quizGroupQuestionDetailOutputModel.setGroupCode(g.getGroupCode());
            quizGroupQuestionDetailOutputModel.setQuestionId(qgq.getQuestionId());
            quizGroupQuestionDetailOutputModel.setQuizGroupId(qgq.getQuizGroupId());
            quizGroupQuestionDetailOutputModel.setQuestionStatement(q.getQuestionContent());
            quizGroupQuestionDetailOutputModels.add(quizGroupQuestionDetailOutputModel);
        }
        return quizGroupQuestionDetailOutputModels;
    }
}
