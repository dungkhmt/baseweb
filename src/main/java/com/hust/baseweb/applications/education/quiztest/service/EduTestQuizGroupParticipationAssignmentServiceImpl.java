package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroup;
import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroupParticipationAssignment;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.QuizTestGroupParticipantAssignmentOutputModel;
import com.hust.baseweb.applications.education.quiztest.repo.EduQuizTestGroupRepo;
import com.hust.baseweb.applications.education.quiztest.repo.EduTestQuizGroupParticipationAssignmentRepo;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EduTestQuizGroupParticipationAssignmentServiceImpl implements EduTestQuizGroupParticipationAssignmentService{

    private EduTestQuizGroupParticipationAssignmentRepo eduTestQuizGroupParticipationAssignmentRepo;
    private EduQuizTestGroupRepo eduQuizTestGroupRepo;

    private UserService userService;

    @Override
    public List<QuizTestGroupParticipantAssignmentOutputModel> getQuizTestGroupParticipant(String testId) {

        List<EduTestQuizGroup> eduTestQuizGroups = eduQuizTestGroupRepo.findByTestId(testId);
        List<UUID> groupIds = eduTestQuizGroups.stream().map(
            eduTestQuizGroup -> eduTestQuizGroup.getQuizGroupId()
        ).collect(Collectors.toList());
        Map<UUID, EduTestQuizGroup> mId2Group = new HashMap();
        for(EduTestQuizGroup e: eduTestQuizGroups){
            mId2Group.put(e.getQuizGroupId(),e);
        }
        List<EduTestQuizGroupParticipationAssignment> eduTestQuizGroupParticipationAssignments
            = eduTestQuizGroupParticipationAssignmentRepo.findAllByQuizGroupIdIn(groupIds);
        List<QuizTestGroupParticipantAssignmentOutputModel> quizTestGroupParticipantAssignmentOutputModels  = new ArrayList<>();
        for(EduTestQuizGroupParticipationAssignment e: eduTestQuizGroupParticipationAssignments){
            String userLoginId = e.getParticipationUserLoginId();
            PersonModel personModel = userService.findPersonByUserLoginId(userLoginId);
            EduTestQuizGroup g = mId2Group.get(e.getQuizGroupId());
            UUID groupId = null;
            String groupCode = "";
            if(g != null){
                groupId = g.getQuizGroupId(); groupCode = g.getGroupCode();
            }
            String fullName = "";
            if(personModel != null){
                fullName = personModel.getFirstName() + " " + personModel.getMiddleName() + " " + personModel.getLastName();
            }
            quizTestGroupParticipantAssignmentOutputModels.add(
                new QuizTestGroupParticipantAssignmentOutputModel(groupId,groupCode,userLoginId,fullName));
        }
        return quizTestGroupParticipantAssignmentOutputModels;
    }
}
