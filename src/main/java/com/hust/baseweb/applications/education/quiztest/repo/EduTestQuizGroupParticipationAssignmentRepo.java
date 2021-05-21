package com.hust.baseweb.applications.education.quiztest.repo;

import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroupParticipationAssignment;
import com.hust.baseweb.applications.education.quiztest.entity.compositeid.CompositeEduTestQuizGroupParticipationAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EduTestQuizGroupParticipationAssignmentRepo extends JpaRepository<EduTestQuizGroupParticipationAssignment, CompositeEduTestQuizGroupParticipationAssignmentId> {

    List<EduTestQuizGroupParticipationAssignment> findEduTestQuizGroupParticipationAssignmentsByParticipationUserLoginId(String userId);

    EduTestQuizGroupParticipationAssignment findByQuizGroupIdAndParticipationUserLoginId(UUID quizGroupId, String participationUserLoginId);


    EduTestQuizGroupParticipationAssignment save(EduTestQuizGroupParticipationAssignment eduTestQuizGroupParticipationAssignment);

}
