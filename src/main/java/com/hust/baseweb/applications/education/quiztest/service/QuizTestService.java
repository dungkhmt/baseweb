package com.hust.baseweb.applications.education.quiztest.service;

import java.util.List;

import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTest;
<<<<<<< HEAD
import com.hust.baseweb.applications.education.quiztest.entity.StudentInTestQueryReturn;
=======
import com.hust.baseweb.applications.education.quiztest.model.EduQuizTestModel;
>>>>>>> a56f48209404c79c44dc8e5f318fb4ed9a317474
import com.hust.baseweb.applications.education.quiztest.model.QuizTestCreateInputModel;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.UserLogin;

import java.util.List;

public interface QuizTestService {
    public EduQuizTest save(QuizTestCreateInputModel input, UserLogin user);
<<<<<<< HEAD
    public List<EduQuizTest> getAllTestByCreateUser(String userLoginId);
    public List<StudentInTestQueryReturn> getAllStudentInTest(String testId);
=======
    public List<EduQuizTestModel> getListQuizByUserId(String userLoginId);
>>>>>>> a56f48209404c79c44dc8e5f318fb4ed9a317474
}
