package com.hust.baseweb.applications.education.model.quiz;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class QuizChooseAnswerInputModel {

    private UUID questionId;

    private List<UUID> chooseAnsIds;
}
