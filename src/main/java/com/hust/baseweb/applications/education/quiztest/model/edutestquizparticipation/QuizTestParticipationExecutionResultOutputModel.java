package com.hust.baseweb.applications.education.quiztest.model.edutestquizparticipation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizTestParticipationExecutionResultOutputModel {
    private String testId;
    private UUID quizGroupId;
    private String participationUserLoginId;
    private String participationFullName;
    private UUID questionId;
    private char result;// Y or N
    private int grade;// diem, ket qua

}
