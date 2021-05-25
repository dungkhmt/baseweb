package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.quiztest.entity.QuizGroupQuestionAssignment;
import com.hust.baseweb.applications.education.quiztest.model.quitestgroupquestion.QuizGroupQuestionDetailOutputModel;

import java.util.List;

public interface QuizGroupQuestionAssignmentService {
    public List<QuizGroupQuestionDetailOutputModel> findAllQuizGroupQuestionAssignmentOfTest(String testId);

}
