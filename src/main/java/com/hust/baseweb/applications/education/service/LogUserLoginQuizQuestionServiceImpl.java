package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.LogUserLoginQuizQuestion;
import com.hust.baseweb.applications.education.repo.ClassRepo;
import com.hust.baseweb.applications.education.repo.LogUserLoginQuizQuestionRepo;
import com.hust.baseweb.applications.education.report.model.quizparticipation.StudentQuizParticipationModel;
import com.hust.baseweb.model.PersonModel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LogUserLoginQuizQuestionServiceImpl implements LogUserLoginQuizQuestionService {

    private final LogUserLoginQuizQuestionRepo logUserLoginQuizQuestionRepo;

    private final ClassRepo classRepo;

    @Override
    public Page<StudentQuizParticipationModel> findByClassId(UUID classId, Integer page, Integer size) {
        Pageable sortedByCreatedStampDsc =
            PageRequest.of(page, size, Sort.by("createStamp").descending());
        Page<LogUserLoginQuizQuestion> logs = logUserLoginQuizQuestionRepo.findAll(sortedByCreatedStampDsc);

        EduClass eduClass = classRepo.findById(classId).orElse(null);
        String courseId = "";
        String courseName = "";

        if (eduClass != null) {
            courseId = eduClass.getEduCourse().getId();
            courseName = eduClass.getEduCourse().getName();
        }

        HashMap<String, PersonModel> mUserLoginId2PersonModel =
            LogUserLoginCourseChapterMaterialServiceImpl.mUserLoginId2PersonModel;
        //CacheQuizCourseTopic cacheQuizCourseTopic = QuizCourseTopicServiceImpl.cacheQuizCourseTopic;

        // Copy instance to use in stream
        String finalCourseId = courseId;
        String finalCourseName = courseName;

        Page<StudentQuizParticipationModel> studentQuizParticipationModels = logs.map(log -> {
            //PersonModel personModel = userService.findPersonByUserLoginId(log.getUserLoginId());
            PersonModel personModel = mUserLoginId2PersonModel.get(log.getUserLoginId());
            //QuizCourseTopic q = cacheQuizCourseTopic.get(log.getQuestionId());
            int grade = 0;
            //if(log.getIsCorrectAnswer() == 'Y')
            if (log.getIsCorrectAnswer() != null) {
                if (log.getIsCorrectAnswer().equals("Y")) {
                    grade = 1;
                }
            }

            String fullName = "";
            if (personModel != null) {
                fullName = personModel.getLastName() +
                           " " +
                           personModel.getMiddleName() +
                           " " +
                           personModel.getFirstName();
            }

            return new StudentQuizParticipationModel(
                log.getUserLoginId(),
                fullName,
                (log.getClassId() != null ? log.getClassId().toString() : ""),
                log.getClassCode() + "",
                (log.getQuestionId() != null ? log.getQuestionId().toString() : ""),
                finalCourseId,
                finalCourseName,
                log.getQuestionTopicName(),
                log.getQuestionTopicId(),
                //q.getQuizCourseTopicName(),
                //q.getQuizCourseTopicId(),
                grade,
                log.getCreateStamp()
            );
        });

        return studentQuizParticipationModels;
    }
}
