package com.hust.baseweb.applications.education.programsubmisson.repo;

import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProgramSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContestProgramSubmissionRepo extends JpaRepository<ContestProgramSubmission, UUID> {
    ContestProgramSubmission save(ContestProgramSubmission contestProgramSubmission);
    List<ContestProgramSubmission> findAllByContestIdAndProblemIdAndSubmittedByUserLoginId(String contestId, String problemId, String submittedByUserLoginId);
    List<ContestProgramSubmission> findAllBySubmittedByUserLoginId(String submittedByUserLoginId);
    List<ContestProgramSubmission> findAllByProblemId(String problemId);
    ContestProgramSubmission findByContestProgramSubmissionId(UUID contestProgramSubmissionId);
}
