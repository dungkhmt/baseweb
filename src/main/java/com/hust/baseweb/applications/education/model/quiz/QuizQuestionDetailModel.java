package com.hust.baseweb.applications.education.model.quiz;

import com.hust.baseweb.applications.education.entity.QuizChoiceAnswer;
import com.hust.baseweb.applications.education.entity.QuizCourseTopic;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class QuizQuestionDetailModel {

    private UUID questionId;

    private String statement;

    private QuizCourseTopic quizCourseTopic;

    private String levelId;

    List<QuizChoiceAnswer> quizChoiceAnswerList;
}
