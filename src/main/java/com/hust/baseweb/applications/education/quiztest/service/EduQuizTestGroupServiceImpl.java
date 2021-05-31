package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.entity.EduCourse;
import com.hust.baseweb.applications.education.entity.QuizChoiceAnswer;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
import com.hust.baseweb.applications.education.quiztest.entity.*;
import com.hust.baseweb.applications.education.quiztest.model.QuizGroupTestDetailModel;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.GenerateQuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.repo.*;
import com.hust.baseweb.applications.education.repo.EduCourseRepo;
import com.hust.baseweb.applications.education.service.QuizQuestionService;
import com.hust.baseweb.applications.education.suggesttimetable.repo.CourseRepo;
import com.hust.baseweb.service.UserService;
import com.hust.baseweb.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class EduQuizTestGroupServiceImpl implements EduQuizTestGroupService{
    private EduQuizTestGroupRepo eduQuizTestGroupRepo;
    private UserService userService;
    private QuizQuestionService quizQuestionService;
    private EduQuizTestRepo eduQuizTestRepo;
    private EduCourseRepo eduCourseRepo;
    private QuizGroupQuestionAssignmentRepo quizGroupQuestionAssignmentRepo;
    private EduTestQuizGroupParticipationAssignmentRepo eduTestQuizGroupParticipationAssignmentRepo;
    private QuizGroupQuestionParticipationExecutionChoiceRepo quizGroupQuestionParticipationExecutionChoiceRepo;
    private static Random R = new Random();
    @Override
    public List<EduTestQuizGroup> generateQuizTestGroups(GenerateQuizTestGroupInputModel input) {
        List<EduTestQuizGroup> eduTestQuizGroups = eduQuizTestGroupRepo.findByTestId(input.getQuizTestId());
        List<EduTestQuizGroup> eduTestQuizGroupList = new ArrayList<EduTestQuizGroup>();
        String[] codes  = new String[eduTestQuizGroups.size()];
        for(int i = 0; i < eduTestQuizGroups.size(); i++){
            codes[i] = eduTestQuizGroups.get(i).getGroupCode();
        }
        String[] newCodes = CommonUtils.generateNextSeqId(codes, input.getNumberOfQuizTestGroups());

        for(int i = 0; i < newCodes.length; i++){
            log.info("generateQuizTestGroups, gen newCode " + newCodes[i]);
            EduTestQuizGroup eduTestQuizGroup = new EduTestQuizGroup();
            eduTestQuizGroup.setTestId(input.getQuizTestId());
            eduTestQuizGroup.setGroupCode(newCodes[i]);

            eduTestQuizGroup = eduQuizTestGroupRepo.save(eduTestQuizGroup);

            eduTestQuizGroupList.add(eduTestQuizGroup);
        }
        return eduTestQuizGroupList;
    }
    public QuizGroupTestDetailModel getTestGroupQuestionDetail(Principal principal, String testID){

        EduQuizTest test = eduQuizTestRepo.findById(testID).get();
        String courseName = eduCourseRepo.findById(test.getCourseId()).get().getName();
        List<QuizQuestionDetailModel> listQuestions = new ArrayList<>();

        QuizGroupTestDetailModel testDetail = new QuizGroupTestDetailModel();
        testDetail.setTestId(testID);
        testDetail.setTestName(test.getTestName());
        testDetail.setDuration(test.getDuration());
        SimpleDateFormat  formatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        String strDate = formatter.format(test.getScheduleDatetime());
        testDetail.setScheduleDatetime(strDate);
        testDetail.setCourseName(courseName);



        List<EduTestQuizGroupParticipationAssignment> listGroupAsignment   = eduTestQuizGroupParticipationAssignmentRepo.findEduTestQuizGroupParticipationAssignmentsByParticipationUserLoginId(principal.getName());
        //System.out.println(listGroupAsignment.size());
        EduTestQuizGroup eduTestQuizGroup = null;//new EduTestQuizGroup();
        for (EduTestQuizGroupParticipationAssignment ele: listGroupAsignment) {
            eduTestQuizGroup = eduQuizTestGroupRepo.findEduTestQuizGroupByTestIdAndQuizGroupId(testID,ele.getQuizGroupId());
            if (eduTestQuizGroup != null) {
                break;
            }
        }
        if (eduTestQuizGroup == null){
            testDetail.setListQuestion(null);
            testDetail.setQuizGroupId(null);
            testDetail.setGroupCode(null);
            testDetail.setParticipationExecutionChoice(null);
            return testDetail;
        }
        UUID groupId = eduTestQuizGroup.getQuizGroupId();
        String groupCode = eduTestQuizGroup.getGroupCode();
        List<QuizGroupQuestionAssignment> tmpl = quizGroupQuestionAssignmentRepo.findQuizGroupQuestionAssignmentsByQuizGroupId(groupId);
        //System.out.println(tmpl.size());
        if(tmpl.size() == 0 ){
            testDetail.setListQuestion(null);
            testDetail.setQuizGroupId(groupId.toString());
            testDetail.setGroupCode(null);
            testDetail.setParticipationExecutionChoice(null);
            return testDetail;
        }
        tmpl.forEach( asign -> {
            //System.out.println("here ");
            QuizQuestionDetailModel quizQuestion = quizQuestionService.findQuizDetail(asign.getQuestionId());
            //System.out.println("ok ");
            listQuestions.add(quizQuestion);
        });


        List<QuizGroupQuestionParticipationExecutionChoice> listChoice =   quizGroupQuestionParticipationExecutionChoiceRepo.findQuizGroupQuestionParticipationExecutionChoicesByParticipationUserLoginIdAndQuizGroupId(
            principal.getName(), groupId);
        Map<String , List<UUID>> participationExecutionChoice = new HashMap<>();
        listChoice.forEach( ele -> {
            String quesId = ele.getQuestionId().toString();
            if(participationExecutionChoice.containsKey(quesId)){
                participationExecutionChoice.get(quesId).add(ele.getChoiceAnswerId());
            }else {
                List<UUID> tmp = new ArrayList<>();
                tmp.add(ele.getChoiceAnswerId());
                participationExecutionChoice.put(quesId, tmp);
            }
        });


        // swap randomly listQUestions
        QuizQuestionDetailModel[] arr = new QuizQuestionDetailModel[listQuestions.size()];
        for(int i = 0; i < arr.length; i++){
            arr[i] = listQuestions.get(i);
        }
        for(int k = 0; k < arr.length; k++){
            int i = R.nextInt(arr.length);
            int j = R.nextInt(arr.length);
            QuizQuestionDetailModel tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
        }
        listQuestions.clear();
        for(int i = 0; i < arr.length; i++) {
            listQuestions.add(arr[i]);
            for(QuizChoiceAnswer a: arr[i].getQuizChoiceAnswerList()){
                a.setIsCorrectAnswer('-');
            }
        }

        testDetail.setListQuestion(listQuestions);
        testDetail.setQuizGroupId(groupId.toString());
        testDetail.setGroupCode(groupCode);
        testDetail.setParticipationExecutionChoice(participationExecutionChoice);
        return testDetail;
    }
}
