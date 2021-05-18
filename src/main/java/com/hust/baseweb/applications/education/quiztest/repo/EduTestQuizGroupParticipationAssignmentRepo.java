package com.hust.baseweb.applications.education.quiztest.repo;

import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroupParticipationAssignment;
import com.hust.baseweb.applications.education.quiztest.entity.compositeid.CompositeEduTestQuizGroupParticipationAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EduTestQuizGroupParticipationAssignmentRepo extends JpaRepository<EduTestQuizGroupParticipationAssignment, CompositeEduTestQuizGroupParticipationAssignmentId> {

    List<EduTestQuizGroupParticipationAssignment> findEduTestQuizGroupParticipationAssignmentsByParticipationUserLoginId(String userId);
}
