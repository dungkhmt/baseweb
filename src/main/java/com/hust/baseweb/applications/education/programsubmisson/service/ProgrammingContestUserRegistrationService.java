package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblem;
import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContestUserRegistration;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateProgrammingContestUserRegistrationInputModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProgrammingContestUserRegistrationService {
    ProgrammingContestUserRegistration save(CreateProgrammingContestUserRegistrationInputModel input);
    ProgrammingContestUserRegistration updateStatus(CreateProgrammingContestUserRegistrationInputModel input);
    List<ProgrammingContestUserRegistration> findAll();
    List<ProgrammingContestUserRegistration> findByUserLoginIdAndStatusId(String userLoginId, String statusId);

    List<ProgrammingContestUserRegistration> findByContestIdAndStatusId(String contestId, String statusId);

    List<ContestProblem> getProblemsOfContestAndUser(String contestId, String userLoginId);

}
