package com.hust.baseweb.applications.education.quiztest.model.quiztestgroup;

import java.math.BigInteger;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizTestGroupInfoModel {
    String quizGroupId;
    String groupCode;
    String note;
    int numQuestion;
    int numStudent;
}
