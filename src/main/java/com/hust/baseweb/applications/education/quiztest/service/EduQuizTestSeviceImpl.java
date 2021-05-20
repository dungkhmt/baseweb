package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizParticipant;
import com.hust.baseweb.applications.education.quiztest.model.EduQuizTestModel;
import com.hust.baseweb.applications.education.quiztest.repo.EduTestQuizParticipantRepo;
import com.hust.baseweb.applications.education.quiztest.repo.EduQuizTestRepo.StudentInfo;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTest;
import com.hust.baseweb.applications.education.quiztest.model.QuizTestCreateInputModel;
import com.hust.baseweb.applications.education.quiztest.model.StudentInTestQueryReturnModel;
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

    @Override
    public List<EduQuizTest> getAllTestByCreateUser(String userLoginId) {
        return repo.findByCreateUser(userLoginId);
    }

    @Override
    public List<StudentInTestQueryReturnModel> getAllStudentInTest(String testId) {
        List<StudentInfo> list = repo.findAllStudentInTest(testId);

        List<StudentInTestQueryReturnModel> re = new ArrayList<>();

        for (StudentInfo studentInfo : list) {
            StudentInTestQueryReturnModel temp = new StudentInTestQueryReturnModel();
            temp.setFullName(studentInfo.getFull_name());
            temp.setTestId(studentInfo.getTest_id());
            temp.setEmail(studentInfo.getEmail());
            temp.setUserLoginId(studentInfo.getUser_login_id());
            temp.setStatusId(studentInfo.getStatus_id());
            re.add(temp);
        }

        return re;
    }

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
    }

    
    @Override
    public Integer rejectStudentsInTest(String testId, String[] userLoginId) {
        Integer re = 0;
        for (String student : userLoginId) {
            re += repo.rejectStudentInTest(testId, student);
        }
        return re;
    }

    @Override
    public EduQuizTest getQuizTestById(String testId) {
        Optional<EduQuizTest> re = repo.findById(testId);

        if(re.isPresent()) return re.get();
        else return null;
    }

    @Override
    public Integer acceptStudentsInTest(String testId, String[] userLoginId) {
        Integer re = 0;
        for (String student : userLoginId) {
            re += repo.acceptStudentInTest(testId, student);
        }
        return re;
    }

}
