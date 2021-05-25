package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroup;
import com.hust.baseweb.applications.education.quiztest.entity.QuizGroupQuestionAssignment;
import com.hust.baseweb.applications.education.quiztest.repo.EduQuizTestGroupRepo;
import com.hust.baseweb.applications.education.quiztest.repo.QuizGroupQuestionAssignmentRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuizGroupQuestionAssignmentServiceImpl implements QuizGroupQuestionAssignmentService{
    private QuizGroupQuestionAssignmentRepo quizGroupQuestionAssignmentRepo;
    private EduQuizTestGroupRepo eduQuizTestGroupRepo;
    @Override
    public List<QuizGroupQuestionAssignment> findAllQuizGroupQuestionAssignmentOfTest(String testId) {
        List<EduTestQuizGroup> eduQuizTestGroups = eduQuizTestGroupRepo.findByTestId(testId);
        log.info("findAllQuizGroupQuestionAssignmentOfTest, groups.sz = " + eduQuizTestGroups.size());
        List<UUID> quizGroupIds= eduQuizTestGroups.stream().map(eduQuizTestGroup -> eduQuizTestGroup.getQuizGroupId()).collect(
            Collectors.toList());
        for(UUID id: quizGroupIds){
            log.info("findAllQuizGroupQuestionAssignmentOfTest, groupId " + id);
        }
        List<QuizGroupQuestionAssignment> quizGroupQuestionAssignment = quizGroupQuestionAssignmentRepo.findAllByQuizGroupIdIn(quizGroupIds);
        log.info("findAllQuizGroupQuestionAssignmentOfTest, return sz = " + quizGroupQuestionAssignment.size());

        return quizGroupQuestionAssignment;
    }
}
