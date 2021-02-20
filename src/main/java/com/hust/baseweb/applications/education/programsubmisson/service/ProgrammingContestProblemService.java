package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContestProblem;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateProgrammingContestProblemInputModel;

import java.util.List;

public interface ProgrammingContestProblemService {
    ProgrammingContestProblem save(CreateProgrammingContestProblemInputModel input);
    List<ProgrammingContestProblem> findAll();
    List<ProgrammingContestProblem> findByContestId(String contestId);
}
