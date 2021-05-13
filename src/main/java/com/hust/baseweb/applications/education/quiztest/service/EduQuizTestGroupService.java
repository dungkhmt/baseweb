package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroup;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.GenerateQuizTestGroupInputModel;

import java.util.List;

public interface EduQuizTestGroupService {
    List<EduTestQuizGroup> generateQuizTestGroups(GenerateQuizTestGroupInputModel input);

}
