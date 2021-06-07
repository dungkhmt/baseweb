package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProgramSubmission;
import com.hust.baseweb.applications.education.programsubmisson.model.SearchProgramSubmissionInputModel;

import java.util.List;

public interface ContestProgramSubmissionService {

    List<ContestProgramSubmission> findAll();

    List<ContestProgramSubmission> findAllBySubmittedByUserLoginId(String submittedByUserLoginId);

    List<ContestProgramSubmission> findAllByProblemId(String problemId);

    List<ContestProgramSubmission> search(SearchProgramSubmissionInputModel input);

    //ContestProgramSubmission findByContestProgramSubmissionId (UUID contestProgramSubmissionId);
}
