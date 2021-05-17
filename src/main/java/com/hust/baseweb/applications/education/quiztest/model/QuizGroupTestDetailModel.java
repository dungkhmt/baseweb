package com.hust.baseweb.applications.education.quiztest.model;


import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class QuizGroupTestDetailModel {
    private String testId;

    private String testName;

    private Date scheduleDatetime;

    private String  courseName;

    private Integer duration;

    private String quizGroupId;
    private String groupCode;


    private List<QuizQuestionDetailModel> listQuestion;


}
