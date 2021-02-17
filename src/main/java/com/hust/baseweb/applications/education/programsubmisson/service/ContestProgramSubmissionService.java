package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProgramSubmission;
import com.hust.baseweb.applications.education.programsubmisson.model.SubmittedProgramDetailModel;

import java.util.List;
import java.util.UUID;

public interface ContestProgramSubmissionService {
    List<ContestProgramSubmission> findAll();
    List<ContestProgramSubmission> findAllBySubmittedByUserLoginId(String submittedByUserLoginId);
    List<ContestProgramSubmission> findAllByProblemId(String problemId);

    //ContestProgramSubmission findByContestProgramSubmissionId (UUID contestProgramSubmissionId);
}
