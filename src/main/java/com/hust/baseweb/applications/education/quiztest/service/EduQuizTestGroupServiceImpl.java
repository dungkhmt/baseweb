package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.entity.EduCourse;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTest;
import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroup;
import com.hust.baseweb.applications.education.quiztest.entity.QuizGroupQuestionAssignment;
import com.hust.baseweb.applications.education.quiztest.model.QuizGroupTestDetailModel;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.GenerateQuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.repo.EduQuizTestGroupRepo;
import com.hust.baseweb.applications.education.quiztest.repo.EduQuizTestRepo;
import com.hust.baseweb.applications.education.quiztest.repo.EduTestQuizGroupParticipationAssignmentRepo;
import com.hust.baseweb.applications.education.quiztest.repo.QuizGroupQuestionAssignmentRepo;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

        UUID groupId;
        groupId = eduTestQuizGroupParticipationAssignmentRepo.findEduTestQuizGroupParticipationAssignmentsByParticipationUserLoginId(principal.getName()).get(0).getQuizGroupId();
        System.out.println("here0 ");
        List<QuizGroupQuestionAssignment> tmpl = quizGroupQuestionAssignmentRepo.findQuizGroupQuestionAssignmentsByQuizGroupId(groupId);
        tmpl.forEach( asign -> {
            System.out.println("here ");
            QuizQuestionDetailModel quizQuestion = quizQuestionService.findQuizDetail(asign.getQuestionId());
            System.out.println("ok ");
            listQuestions.add(quizQuestion);
        });
        String groupCode =  eduQuizTestGroupRepo.findEduTestQuizGroupByTestIdAndQuizGroupId(testID,groupId).getGroupCode();
        QuizGroupTestDetailModel testDetail = new QuizGroupTestDetailModel();
        testDetail.setTestId(testID);
        testDetail.setListQuestion(listQuestions);
        testDetail.setQuizGroupId(groupId.toString());
        testDetail.setGroupCode(groupCode);
        testDetail.setCourseName(courseName);
        testDetail.setTestName(test.getTestName());
        testDetail.setDuration(test.getDuration());
        testDetail.setScheduleDatetime(test.getScheduleDatetime());
        return testDetail;
    }
}
