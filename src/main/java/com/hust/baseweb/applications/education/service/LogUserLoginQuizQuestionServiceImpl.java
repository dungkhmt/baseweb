package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.cache.CacheQuizCourseTopic;
import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.LogUserLoginQuizQuestion;
import com.hust.baseweb.applications.education.entity.QuizCourseTopic;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        HashMap<String, PersonModel> mUserLoginId2PersonModel =
            LogUserLoginCourseChapterMaterialServiceImpl.mUserLoginId2PersonModel;
        //CacheQuizCourseTopic cacheQuizCourseTopic = QuizCourseTopicServiceImpl.cacheQuizCourseTopic;

        List<StudentQuizParticipationModel> studentQuizParticipationModels = new ArrayList();
        for (LogUserLoginQuizQuestion e : logUserLoginQuizQuestions) {
            //PersonModel personModel = userService.findPersonByUserLoginId(e.getUserLoginId());
            PersonModel personModel = mUserLoginId2PersonModel.get(e.getUserLoginId());
            //QuizCourseTopic q = cacheQuizCourseTopic.get(e.getQuestionId());
            int grade = 0;
            //if(e.getIsCorrectAnswer() == 'Y')
            if(e.getIsCorrectAnswer() != null)
                if(e.getIsCorrectAnswer().equals("Y"))
                    grade = 1;
            String fullName = "";
            if(personModel != null){
                fullName = personModel.getLastName() + " " + personModel.getMiddleName() + " " + personModel.getFirstName();
            }
            studentQuizParticipationModels.add(new StudentQuizParticipationModel(
                e.getUserLoginId(),
                fullName,
                (e.getClassId() != null ? e.getClassId().toString() : ""),
                e.getClassCode() + "",
                (e.getQuestionId() != null ? e.getQuestionId().toString() : ""),
                courseId,
                courseName,
                e.getQuestionTopicName(),
                e.getQuestionTopicId(),
                //q.getQuizCourseTopicName(),
                //q.getQuizCourseTopicId(),
                grade,
                //e.getCreateStamp()
                df.format(e.getCreateStamp())
            ));
            //log.info("findAllByClassId, datetime = " + df.format(e.getCreateStamp()));
        }
        return studentQuizParticipationModels;
    }
}
