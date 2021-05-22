package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.classmanagement.service.ClassService;
import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.quiztest.entity.*;
import com.hust.baseweb.applications.education.quiztest.model.EduQuizTestModel;
import com.hust.baseweb.applications.education.quiztest.model.quitestgroupquestion.AutoAssignQuestion2QuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.AutoAssignParticipants2QuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.repo.*;
import com.hust.baseweb.applications.education.quiztest.repo.EduQuizTestRepo.StudentInfo;

import com.hust.baseweb.applications.education.service.QuizQuestionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Optional;

import com.hust.baseweb.applications.education.quiztest.model.QuizTestCreateInputModel;
import com.hust.baseweb.applications.education.quiztest.model.StudentInTestQueryReturnModel;
import com.hust.baseweb.entity.UserLogin;

import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EduQuizTestSeviceImpl implements QuizTestService{

    EduQuizTestRepo repo;
    EduTestQuizParticipantRepo eduTestQuizParticipantRepo;
    EduQuizTestGroupRepo eduQuizTestGroupRepo;
    EduTestQuizGroupParticipationAssignmentRepo eduTestQuizGroupParticipationAssignmentRepo;
    QuizQuestionService quizQuestionService;
    ClassService classService;
    QuizGroupQuestionAssignmentRepo quizGroupQuestionAssignmentRepo;
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

        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        List<EduQuizTestModel> listModel = new ArrayList<>();
        // or find by user id??
        List<EduQuizTest> listEdu = repo.findAll();
        for (EduQuizTest eduEntity:
             listEdu) {
            EduQuizTestModel eduModel = new EduQuizTestModel();
            eduModel.setTestId(eduEntity.getTestId());
            eduModel.setTestName(eduEntity.getTestName());
            eduModel.setCourseId(eduEntity.getCourseId());

            String strDate = formatter.format(eduEntity.getScheduleDatetime());
            eduModel.setScheduleDatetime(strDate);
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

    @Transactional
    @Override
    public boolean autoAssignParticipants2QuizTestGroup(AutoAssignParticipants2QuizTestGroupInputModel input) {
        List<EduTestQuizGroup>  eduTestQuizGroups = eduQuizTestGroupRepo.findByTestId(input.getQuizTestId());
        if(eduTestQuizGroups.size() <= 0) return false;

        List<EduTestQuizParticipant> eduTestQuizParticipants = eduTestQuizParticipantRepo
            .findByTestIdAndStatusId(input.getQuizTestId(),EduTestQuizParticipant.STATUS_APPROVED);

        Random R = new Random();

        for(EduTestQuizParticipant p: eduTestQuizParticipants){
            int idx  = R.nextInt(eduTestQuizGroups.size());
            EduTestQuizGroup g = eduTestQuizGroups.get(idx);
            EduTestQuizGroupParticipationAssignment a = eduTestQuizGroupParticipationAssignmentRepo
                .findByQuizGroupIdAndParticipationUserLoginId(g.getQuizGroupId(), p.getParticipantUserLoginId());
            if(a == null) {
                log.info("autoAssignParticipants2QuizTestGroup, assignment " + g.getQuizGroupId() + "," + p.getParticipantUserLoginId() + " not exists -> insert new");
                a = new EduTestQuizGroupParticipationAssignment();
                a.setQuizGroupId(g.getQuizGroupId());
                a.setParticipationUserLoginId(p.getParticipantUserLoginId());
                a = eduTestQuizGroupParticipationAssignmentRepo.save(a);
            }
        }

        return true;
    }

    @Transactional
    @Override
    public boolean autoAssignQuestion2QuizTestGroup(AutoAssignQuestion2QuizTestGroupInputModel input) {
        List<EduTestQuizGroup>  eduTestQuizGroups = eduQuizTestGroupRepo.findByTestId(input.getQuizTestId());
        if(eduTestQuizGroups.size() <= 0) return false;
        EduClass eduClass = classService.findById(input.getClassId());
        if(eduClass == null){
            log.info("autoAssignQuestion2QuizTestGroup, cannot find class " + input.getClassId());
            return false;
        }

        String courseId = eduClass.getEduCourse().getId();
        List<QuizQuestion> quizQuestions = quizQuestionService.findQuizOfCourse(courseId);

        Random R = new Random();
        for(QuizQuestion q: quizQuestions){
            int idx = R.nextInt(eduTestQuizGroups.size());
            EduTestQuizGroup g = eduTestQuizGroups.get(idx);
            QuizGroupQuestionAssignment qq = quizGroupQuestionAssignmentRepo
                .findByQuestionIdAndQuizGroupId(q.getQuestionId(),g.getQuizGroupId());

            if(qq == null){
                log.info("autoAssignQuestion2QuizTestGroup, record " + q.getQuestionId() + "," + g.getQuizGroupId() + " not exists -> insert new");
                qq = new QuizGroupQuestionAssignment();
                qq.setQuestionId(q.getQuestionId());
                qq.setQuizGroupId(g.getQuizGroupId());
                qq = quizGroupQuestionAssignmentRepo.save(qq);
            }
        }
        return true;
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
