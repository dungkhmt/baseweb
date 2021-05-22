package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.classmanagement.service.ClassService;
import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.quiztest.entity.*;
import com.hust.baseweb.applications.education.quiztest.model.EduQuizTestModel;
import com.hust.baseweb.applications.education.quiztest.model.edutestquizparticipation.QuizTestParticipationExecutionResultOutputModel;
import com.hust.baseweb.applications.education.quiztest.model.quitestgroupquestion.AutoAssignQuestion2QuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.AutoAssignParticipants2QuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.QuizTestGroupInfoModel;
import com.hust.baseweb.applications.education.quiztest.repo.*;
import com.hust.baseweb.applications.education.quiztest.repo.EduQuizTestGroupRepo.QuizTestGroupInfo;
import com.hust.baseweb.applications.education.quiztest.repo.EduQuizTestRepo.StudentInfo;

import com.hust.baseweb.applications.education.service.QuizQuestionService;
import com.hust.baseweb.utils.CommonUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.*;

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
        EduQuizTest eduQuizTest = repo.findById(input.getQuizTestId()).orElse(null);
        if(eduQuizTest == null){
            log.info("autoAssignQuestion2QuizTestGroup, cannot find quizTest " + input.getQuizTestId());
            return false;
        }
        UUID classId = eduQuizTest.getClassId();
        EduClass eduClass = classService.findById(classId);
        if(eduClass == null){
            log.info("autoAssignQuestion2QuizTestGroup, cannot find class " + classId);
            return false;
        }

        String courseId = eduClass.getEduCourse().getId();
        List<QuizQuestion> quizQuestions = quizQuestionService.findQuizOfCourse(courseId);

        HashMap<String, List<QuizQuestion>> mapTopicId2QUestion = new HashMap();
        for(QuizQuestion q: quizQuestions){
            if(q.getStatusId() == QuizQuestion.STATUS_PUBLIC) continue;

            String topicId = q.getQuizCourseTopic().getQuizCourseTopicId();
            if(mapTopicId2QUestion.get(topicId) == null)
                mapTopicId2QUestion.put(topicId, new ArrayList<QuizQuestion>());
            mapTopicId2QUestion.get(topicId).add(q);
        }
        int activeKeys = 0;
        for(String k: mapTopicId2QUestion.keySet()){
            if(mapTopicId2QUestion.get(k) != null) activeKeys++;
        }
        String[] sortedTopicId = new String[activeKeys];
        int I = 0;
        for(String k: mapTopicId2QUestion.keySet()){
            if(mapTopicId2QUestion.get(k) != null){
                sortedTopicId[I] = k;
                I++;
            }
        }

        for(int i = 0; i < sortedTopicId.length; i++){
            for(int j = i+1; j < sortedTopicId.length; j++){
                if(mapTopicId2QUestion.get(sortedTopicId[i]).size() < mapTopicId2QUestion.get(sortedTopicId[j]).size()){
                    String t = sortedTopicId[i]; sortedTopicId[i] = sortedTopicId[j]; sortedTopicId[j] = t;
                }
            }
        }


        HashMap<String, Integer> mTopicId2Num = new HashMap<>();
        for(String k: mapTopicId2QUestion.keySet()){
            mTopicId2Num.put(k,0);
        }
        int amount = input.getNumberQuestions();
        int cnt = 0;
        //for(String k: mapTopicId2QUestion.keySet()){
        for(int i = 0; i < sortedTopicId.length; i++){
            String k = sortedTopicId[i];
            cnt += mapTopicId2QUestion.get(k).size();
        }
        // neu user-input amount > number of availables questions cnt then amount = cnt
        if(amount > cnt){
            amount = cnt;
        }
        int sel_idx = 0;
        while(amount > 0){
            int a = mTopicId2Num.get(sortedTopicId[sel_idx]);
            if(a < mapTopicId2QUestion.get(sortedTopicId[sel_idx]).size()) {
                mTopicId2Num.put(sortedTopicId[sel_idx], a + 1);
                amount--;
            }
            sel_idx++;
            if(sel_idx >= sortedTopicId.length) sel_idx = 0;
        }
        for(int i = 0; i < sortedTopicId.length; i++){
            log.info("autoAssignQuestion2QuizTestGroup, topic " + sortedTopicId[i] + " has "
                     + mapTopicId2QUestion.get(sortedTopicId[i]).size() + " questions" +
                     " select " + mTopicId2Num.get(sortedTopicId[i]));
        }
        Random R = new Random();
        for(EduTestQuizGroup g: eduTestQuizGroups){
            for(int i = 0; i < sortedTopicId.length; i++){
                String topicId = sortedTopicId[i];
                int sz = mTopicId2Num.get(topicId);
                List<QuizQuestion> questions = mapTopicId2QUestion.get(topicId);
                // select randomly sz questions from questions
                int[] idx = CommonUtils.genRandom(sz,questions.size(),R);
                if(idx != null){
                    for(int j = 0; j < idx.length; j++){
                        QuizQuestion q = questions.get(idx[j]);
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
                }
            }
        }
        /*
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
        */
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

    @Override
    public List<QuizTestGroupInfoModel> getQuizTestGroupsInfoByTestId(String testId) {
        List<QuizTestGroupInfo> info = eduQuizTestGroupRepo.findQuizTestGroupsInfo(testId);

        List<QuizTestGroupInfoModel> re = new ArrayList<>();

        for (QuizTestGroupInfo quizTestGroupInfo : info) {
            QuizTestGroupInfoModel temp = new QuizTestGroupInfoModel();
            temp.setQuizGroupId(quizTestGroupInfo.getQuiz_group_id());
            temp.setGroupCode(quizTestGroupInfo.getGroup_code());
            temp.setNote(quizTestGroupInfo.getNote());
            temp.setNumQuestion(quizTestGroupInfo.getNum_question());
            temp.setNumStudent(quizTestGroupInfo.getNum_student());
            re.add(temp);
        }

        return re;
    }

    public Integer deleteQuizTestGroups(String testId, String[] listQuizTestGroupId) {
        Integer re = 0;
        for (String id : listQuizTestGroupId) {
            re += eduQuizTestGroupRepo.deleteQuizTestGroup(testId, UUID.fromString(id));
        }
        return re;
    }

    public List<QuizTestParticipationExecutionResultOutputModel> getQuizTestParticipationExecutionResult(String testId) {
        //TODO by HUY HUY

        return null;
    }
}
