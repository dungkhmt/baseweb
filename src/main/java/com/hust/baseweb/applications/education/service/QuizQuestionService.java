package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.model.quiz.QuizChooseAnswerInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionCreateInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionUpdateInputModel;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface QuizQuestionService {

    QuizQuestion save(QuizQuestionCreateInputModel input);

    QuizQuestion save(UserLogin u, QuizQuestionCreateInputModel input, MultipartFile[] files);

    List<QuizQuestion> findAll();

    List<QuizQuestion> findQuizOfCourse(String courseId);

    QuizQuestionDetailModel findQuizDetail(UUID questionId);

    QuizQuestion changeOpenCloseStatus(UUID questionId);

    public boolean checkAnswer(UserLogin userLogin, QuizChooseAnswerInputModel quizChooseAnswerInputModel);

    public QuizQuestion findById(UUID questionId);

    public QuizQuestion update(UUID questionId, QuizQuestionUpdateInputModel input, MultipartFile[] files);
}
