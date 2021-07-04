package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblem;
import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContestUserRegistration;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateProgrammingContestUserRegistrationInputModel;
import com.hust.baseweb.applications.education.programsubmisson.model.SearchProgrammingContestUserRegistrationInputModel;

import java.util.List;

public interface ProgrammingContestUserRegistrationService {

    ProgrammingContestUserRegistration save(CreateProgrammingContestUserRegistrationInputModel input);

    ProgrammingContestUserRegistration updateStatus(CreateProgrammingContestUserRegistrationInputModel input);

    List<ProgrammingContestUserRegistration> findAll();

    List<ProgrammingContestUserRegistration> findByUserLoginIdAndStatusId(String userLoginId, String statusId);

    List<ProgrammingContestUserRegistration> findByUserLoginId(String userLoginId);

    List<ProgrammingContestUserRegistration> search(SearchProgrammingContestUserRegistrationInputModel input);

    List<ProgrammingContestUserRegistration> findByContestIdAndStatusId(String contestId, String statusId);

    List<ContestProblem> getProblemsOfContestAndUser(String contestId, String userLoginId);

}
