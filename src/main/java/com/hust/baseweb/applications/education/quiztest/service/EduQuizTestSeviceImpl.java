package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizParticipant;
import com.hust.baseweb.applications.education.quiztest.model.EduQuizTestModel;
import com.hust.baseweb.applications.education.quiztest.repo.EduTestQuizParticipantRepo;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTest;
import com.hust.baseweb.applications.education.quiztest.entity.StudentInTestQueryReturn;
import com.hust.baseweb.applications.education.quiztest.model.QuizTestCreateInputModel;
import com.hust.baseweb.applications.education.quiztest.repo.EduQuizTestRepo;
import com.hust.baseweb.entity.UserLogin;

import org.springframework.beans.factory.annotation.Autowired;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EduQuizTestSeviceImpl implements QuizTestService{

    EduQuizTestRepo repo;
    EduTestQuizParticipantRepo eduTestQuizParticipantRepo;
    @Override
    public EduQuizTest save(QuizTestCreateInputModel input, UserLogin user) {
        EduQuizTest newRecord = new EduQuizTest();

        newRecord.setTestName(input.getTestName());
        newRecord.setCourseId(input.getCourseId());
        newRecord.setCreatedByUserLoginId(user.getUserLoginId());
        newRecord.setDuration(input.getDuration());
        newRecord.setScheduleDatetime(input.getScheduleDatetime());
        newRecord.setStatusId("");
        newRecord.setTestId(input.getTestId());
        newRecord.setClassId(input.getClassId());
        newRecord.setCreatedStamp(new Date());
        newRecord.setLastUpdatedStamp(new Date());

        return repo.save(newRecord);
    }
<<<<<<< HEAD

    @Override
    public List<EduQuizTest> getAllTestByCreateUser(String userLoginId) {
        return repo.findByCreateUser(userLoginId);
    }

    @Override
    public List<StudentInTestQueryReturn> getAllStudentInTest(String testId) {
        return repo.findAllStudentInTest(testId);
=======
    @Override
    public List<EduQuizTestModel> getListQuizByUserId(String userLoginId){

        List<EduQuizTestModel> listModel = new ArrayList<>();
        // or find by user id??
        List<EduQuizTest> listEdu = repo.findAll();
        for (EduQuizTest eduEntity:
             listEdu) {
            EduQuizTestModel eduModel = new EduQuizTestModel();
            eduModel.setTestId(eduEntity.getTestId());
            eduModel.setTestName(eduEntity.getTestName());
            eduModel.setCourseId(eduEntity.getCourseId());
            eduModel.setScheduleDatetime(eduEntity.getScheduleDatetime());
            //eduModel.setStatusId(eduEntity.getStatusId());
            List<EduTestQuizParticipant> eduTestQuizParticipants = eduTestQuizParticipantRepo
                .findByTestIdAndParticipantUserLoginId(eduEntity.getTestId(), userLoginId);
            if(eduTestQuizParticipants != null && eduTestQuizParticipants.size() > 0){
                eduModel.setStatusId(eduTestQuizParticipants.get(0).getStatusId());
            }
            else{
                eduModel.setStatusId(null);
            }

            listModel.add(eduModel);
        }
        return listModel;
>>>>>>> a56f48209404c79c44dc8e5f318fb4ed9a317474
    }
}
