package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTest;
import com.hust.baseweb.applications.education.quiztest.model.EduQuizTestModel;
import com.hust.baseweb.applications.education.quiztest.model.QuizTestCreateInputModel;
import com.hust.baseweb.entity.UserLogin;

import java.util.List;

public interface QuizTestService {
    public EduQuizTest save(QuizTestCreateInputModel input, UserLogin user);
    public List<EduQuizTestModel> getListQuizByUserId(String userLoginId);
}
