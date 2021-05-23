package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.QuizTestGroupParticipantAssignmentOutputModel;

import java.util.List;

public interface EduTestQuizGroupParticipationAssignmentService {
    public List<QuizTestGroupParticipantAssignmentOutputModel> getQuizTestGroupParticipant(String testId);

}
