package com.hust.baseweb.applications.education.quiztest.service;

import java.util.List;

import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTest;
import com.hust.baseweb.applications.education.quiztest.entity.StudentInTestQueryReturn;
import com.hust.baseweb.applications.education.quiztest.model.QuizTestCreateInputModel;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.UserLogin;

public interface QuizTestService {
    public EduQuizTest save(QuizTestCreateInputModel input, UserLogin user);
    public List<EduQuizTest> getAllTestByCreateUser(String userLoginId);
    public List<StudentInTestQueryReturn> getAllStudentInTest(String testId);
}
