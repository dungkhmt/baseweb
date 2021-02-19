package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContestUserRegistration;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateProgrammingContestUserRegistrationInputModel;

import java.util.List;

public interface ProgrammingContestUserRegistrationService {
    ProgrammingContestUserRegistration save(CreateProgrammingContestUserRegistrationInputModel input);
    List<ProgrammingContestUserRegistration> findAll();
}
