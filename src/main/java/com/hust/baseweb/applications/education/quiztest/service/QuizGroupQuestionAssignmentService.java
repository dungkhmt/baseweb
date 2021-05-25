package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.quiztest.entity.QuizGroupQuestionAssignment;

import java.util.List;

public interface QuizGroupQuestionAssignmentService {
    public List<QuizGroupQuestionAssignment> findAllQuizGroupQuestionAssignmentOfTest(String testId);

}
