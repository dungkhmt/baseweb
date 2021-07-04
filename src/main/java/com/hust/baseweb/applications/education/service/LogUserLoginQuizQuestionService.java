package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.report.model.quizparticipation.StudentQuizParticipationModel;

import java.util.List;
import java.util.UUID;

public interface LogUserLoginQuizQuestionService {

    public List<StudentQuizParticipationModel> findAllByClassId(UUID classId);
}
