package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblem;
import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContest;
import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContestUserRegistration;
import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContestUserRegistrationProblem;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateProgrammingContestUserRegistrationInputModel;
import com.hust.baseweb.applications.education.programsubmisson.model.SearchProgrammingContestUserRegistrationInputModel;
import com.hust.baseweb.applications.education.programsubmisson.repo.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProgrammingContestUserRegistrationServiceImpl implements ProgrammingContestUserRegistrationService {

    private ProgrammingContestUserRegistrationRepo programmingContestUserRegistrationRepo;
    private ContestProblemRepo contestProblemRepo;
    private ProgrammingContestUserRegistrationProblemRepo programmingContestUserRegistrationProblemRepo;
    private ProgrammingContestProblemRepo programmingContestProblemRepo;
    private ProgrammingContestRepo programmingContestRepo;
    private ProgrammingContestService programmingContestService;

    @Override
    public ProgrammingContestUserRegistration save(CreateProgrammingContestUserRegistrationInputModel input) {

        ProgrammingContestUserRegistration programmingContestUserRegistration = new ProgrammingContestUserRegistration();
        programmingContestUserRegistration.setContestId(input.getContestId());
        programmingContestUserRegistration.setUserLoginId(input.getUserLoginId());
        programmingContestUserRegistration.setStatusId(input.getStatusId());
        programmingContestUserRegistration = programmingContestUserRegistrationRepo.save(
            programmingContestUserRegistration);
        return programmingContestUserRegistration;
    }

    @Transactional
    @Modifying
    @Override
    public ProgrammingContestUserRegistration updateStatus(CreateProgrammingContestUserRegistrationInputModel input) {
        ProgrammingContestUserRegistration programmingContestUserRegistration = programmingContestUserRegistrationRepo
            .findByContestIdAndUserLoginId(input.getContestId(), input.getUserLoginId());

        if (programmingContestUserRegistration == null) {
            log.info("updateStatus, cannot find programmingContestUserRegistration with contestId = " +
                     input.getContestId() +
                     " userLoginId = " +
                     input.getUserLoginId());
        } else {
            programmingContestUserRegistration.setStatusId(input.getStatusId());
            log.info("updateStatus, obj status = " +
                     programmingContestUserRegistration.getStatusId() +
                     ", contestId = "
                     +
                     programmingContestUserRegistration.getContestId() +
                     ", userLoginId = " +
                     programmingContestUserRegistration.getUserLoginId());
            programmingContestUserRegistration = programmingContestUserRegistrationRepo.save(
                programmingContestUserRegistration);
            log.info("updateStatus, status = " + programmingContestUserRegistration.getStatusId() + " updated");
        }

        //programmingContestUserRegistrationRepo.updateStatus(input.getContestId(),input.getUserLoginId(),"APPROVED");

        log.info("updateStatus finished");


        return null;//programmingContestUserRegistration;
    }

    @Override
    public List<ProgrammingContestUserRegistration> findAll() {
        return programmingContestUserRegistrationRepo.findAll();
    }

    @Override
    public List<ProgrammingContestUserRegistration> findByUserLoginIdAndStatusId(String userLoginId, String statusId) {
        log.info("findByUserLoginIdAndStatusId, userLoginId  = " + userLoginId + ", statudId = " + statusId);
        return programmingContestUserRegistrationRepo.findByUserLoginIdAndStatusId(userLoginId, statusId);
    }

    @Override
    public List<ProgrammingContestUserRegistration> findByUserLoginId(String userLoginId) {
        return programmingContestUserRegistrationRepo.findByUserLoginId(userLoginId);
    }

    @Override
    public List<ProgrammingContestUserRegistration> search(SearchProgrammingContestUserRegistrationInputModel input) {
        log.info("search, contestId = " + input.getContestId() + " userLoginId = " + input.getUserLoginId() +
                 " statusId = " + input.getStatusId());

        if (input.getContestId() == null && input.getStatusId() == null && input.getUserLoginId() != null) {
            return programmingContestUserRegistrationRepo.findByUserLoginId(input.getUserLoginId());
        } else if (input.getUserLoginId() == null && input.getContestId() != null && input.getStatusId() != null) {
            return programmingContestUserRegistrationRepo.findByContestIdAndStatusId(
                input.getContestId(),
                input.getStatusId());
        } else {
            return programmingContestUserRegistrationRepo.findAll();
        }
        //return null;
    }

    @Override
    public List<ProgrammingContestUserRegistration> findByContestIdAndStatusId(String contestId, String statusId) {
        return programmingContestUserRegistrationRepo.findByContestIdAndStatusId(contestId, statusId);
    }


    @Override
    public List<ContestProblem> getProblemsOfContestAndUser(String contestId, String userLoginId) {
        ProgrammingContest programmingContest = programmingContestRepo.findByContestId(contestId);
        if (programmingContest == null) {
            log.info("getProblemsOfContestAndUser, cannot find contest " + contestId);
            return null;
        }
        log.info("getProblemsOfContestAndUser, contestTypeId = " + programmingContest.getContestTypeId());

        List<ContestProblem> contestProblems = null;
        if (programmingContest.getContestTypeId().equals(ProgrammingContest.CONTEST_TYPE_PARTICIPANT_IDENTICAL)) {
            contestProblems = programmingContestService.getProblemsOfContest(contestId);
        } else if (programmingContest.getContestTypeId().equals(ProgrammingContest.CONTEST_TYPE_PARTICIPANT_SPECIFIC)) {
            List<ProgrammingContestUserRegistrationProblem> programmingContestUserRegistrationProblems =
                programmingContestUserRegistrationProblemRepo.findByContestIdAndUserLoginId(contestId, userLoginId);
            contestProblems = new ArrayList<>();
            for (ProgrammingContestUserRegistrationProblem programmingContestUserRegistrationProblem : programmingContestUserRegistrationProblems) {
                String problemId = programmingContestUserRegistrationProblem.getProblemId();
                ContestProblem contestProblem = contestProblemRepo.findByProblemId(problemId);
                contestProblems.add(contestProblem);
            }
        }
        return contestProblems;
    }
}
