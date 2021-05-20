package com.hust.baseweb.applications.education.quiztest.service;

import java.util.List;

import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTest;
import com.hust.baseweb.applications.education.quiztest.model.EduQuizTestModel;
import com.hust.baseweb.applications.education.quiztest.model.QuizTestCreateInputModel;
import com.hust.baseweb.applications.education.quiztest.model.StudentInTestQueryReturnModel;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.UserLogin;

import java.util.List;

public interface QuizTestService {
    public EduQuizTest save(QuizTestCreateInputModel input, UserLogin user);
    public List<EduQuizTest> getAllTestByCreateUser(String userLoginId);
    public List<StudentInTestQueryReturnModel> getAllStudentInTest(String testId);
    public List<EduQuizTestModel> getListQuizByUserId(String userLoginId);
}
