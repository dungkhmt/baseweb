package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.LogUserLoginQuizQuestion;
import com.hust.baseweb.applications.education.repo.ClassRepo;
import com.hust.baseweb.applications.education.repo.LogUserLoginQuizQuestionRepo;
import com.hust.baseweb.applications.education.report.model.quizparticipation.StudentQuizParticipationModel;
import com.hust.baseweb.applications.education.suggesttimetable.repo.CourseRepo;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class LogUserLoginQuizQuestionServiceImpl implements LogUserLoginQuizQuestionService {

    private LogUserLoginQuizQuestionRepo logUserLoginQuizQuestionRepo;
    private UserService userService;
    private ClassRepo classRepo;
    private CourseRepo courseRepo;

    @Override
    public List<StudentQuizParticipationModel> findAllByClassId(UUID classId) {
        List<LogUserLoginQuizQuestion> logUserLoginQuizQuestions = logUserLoginQuizQuestionRepo.findAll();
        EduClass eduClass = classRepo.findById(classId).orElse(null);
        int classCode = 0;
        String courseId = "";
        String courseName = "";
        if (eduClass != null) {
            classCode = eduClass.getCode();
            courseId = eduClass.getEduCourse().getId();
            courseName = eduClass.getEduCourse().getName();
        }

        List<StudentQuizParticipationModel> studentQuizParticipationModels = new ArrayList();
        for (LogUserLoginQuizQuestion e : logUserLoginQuizQuestions) {
            PersonModel personModel = userService.findPersonByUserLoginId(e.getUserLoginId());
            studentQuizParticipationModels.add(new StudentQuizParticipationModel(
                e.getUserLoginId(),
                personModel.getLastName() + " " + personModel.getMiddleName() + " " + personModel.getFirstName(),
                classCode + "",
                e.getQuestionId().toString(),
                courseId,
                courseName,
                0,
                e.getCreateStamp()
            ));
        }
        return studentQuizParticipationModels;
    }
}
