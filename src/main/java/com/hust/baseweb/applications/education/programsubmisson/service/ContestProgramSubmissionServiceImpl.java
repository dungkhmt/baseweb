package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProgramSubmission;
import com.hust.baseweb.applications.education.programsubmisson.model.SearchProgramSubmissionInputModel;
import com.hust.baseweb.applications.education.programsubmisson.repo.ContestProgramSubmissionRepo;
import com.hust.baseweb.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ContestProgramSubmissionServiceImpl implements  ContestProgramSubmissionService {
    @Autowired
    private ContestProgramSubmissionRepo contestProgramSubmissionRepo;

    @Override
    public List<ContestProgramSubmission> findAll() {
        return contestProgramSubmissionRepo.findAll();
    }

    @Override
    public List<ContestProgramSubmission> findAllBySubmittedByUserLoginId(String submittedByUserLoginId) {
        return contestProgramSubmissionRepo.findAllBySubmittedByUserLoginId(submittedByUserLoginId);
    }

    @Override
    public List<ContestProgramSubmission> findAllByProblemId(String problemId) {
        return contestProgramSubmissionRepo.findAllByProblemId(problemId);
    }

    @Override
    public List<ContestProgramSubmission> search(SearchProgramSubmissionInputModel input) {
        log.info("search, contestId = " + input.getContestId() + " problemId = " + input.getProblemId() + " userId = " + input.getSubmittedByUserLoginId());
        if(CommonUtils.nullString(input.getContestId()) &&
           CommonUtils.nullString(input.getProblemId()) &&
           !CommonUtils.nullString(input.getSubmittedByUserLoginId())){
            return contestProgramSubmissionRepo.findAllBySubmittedByUserLoginId(input.getSubmittedByUserLoginId());
        }else if(CommonUtils.nullString(input.getContestId()) &&
                 CommonUtils.nullString(input.getSubmittedByUserLoginId())&&
                 !CommonUtils.nullString(input.getProblemId())){
            return contestProgramSubmissionRepo.findAllByProblemId(input.getProblemId());
        }else if(CommonUtils.nullString(input.getContestId()) &&
                 !CommonUtils.nullString(input.getSubmittedByUserLoginId())&&
                 !CommonUtils.nullString(input.getProblemId())){
            return contestProgramSubmissionRepo
                .findAllBySubmittedByUserLoginIdAndProblemId(input.getSubmittedByUserLoginId(),input.getProblemId());

        }else{
            return contestProgramSubmissionRepo.findAll();
        }
    }
}
