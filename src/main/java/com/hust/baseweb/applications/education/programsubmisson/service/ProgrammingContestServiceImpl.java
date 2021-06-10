package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.*;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateProgrammingContestInputModel;
import com.hust.baseweb.applications.education.programsubmisson.repo.ProgrammingContestRepo;
import com.hust.baseweb.applications.education.programsubmisson.repo.ProgrammingContestUserRegistrationProblemRepo;
import com.hust.baseweb.applications.education.programsubmisson.repo.ProgrammingContestUserRegistrationRepo;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProgrammingContestServiceImpl implements ProgrammingContestService {

    private ProgrammingContestRepo programmingContestRepo;
    private ProgrammingContestProblemService programmingContestProblemService;
    private ContestProblemService contestProblemService;
    private ProgrammingContestUserRegistrationRepo programmingContestUserRegistrationRepo;
    private ProgrammingContestUserRegistrationProblemRepo programmingContestUserRegistrationProblemRepo;

    @Override
    public ProgrammingContest save(UserLogin userLogin, CreateProgrammingContestInputModel input) {
        ProgrammingContest programmingContest = new ProgrammingContest();
        programmingContest.setContestId(input.getContestId());
        programmingContest.setContestName(input.getContestName());
        programmingContest.setCreatedByUserLoginId(userLogin.getUserLoginId());
        programmingContest.setContestTypeId(input.getContestTypeId());
        programmingContest = programmingContestRepo.save(programmingContest);
        return programmingContest;
    }

    @Override
    public List<ProgrammingContest> findAll() {
        List<ProgrammingContest> programmingContests = programmingContestRepo.findAll();
        return programmingContests;
    }

    @Override
    public ProgrammingContest findByContestId(String contestId) {
        return programmingContestRepo.findByContestId(contestId);
    }

    @Override
    public List<ContestProblem> getProblemsOfContest(String contestId) {
        List<ProgrammingContestProblem> programmingContestProblems = programmingContestProblemService.findByContestId(
            contestId);
        List<ContestProblem> contestProblems = new ArrayList();
        for (ProgrammingContestProblem programmingContestProblem : programmingContestProblems) {
            String problemId = programmingContestProblem.getProblemId();
            ContestProblem contestProblem = contestProblemService.findByProblemId(problemId);
            contestProblems.add(contestProblem);
        }
        return contestProblems;
    }

    private String getGroupKey(ContestProblem contestProblem) {
        String gk = "-";
        if (contestProblem.getLevelId() != null && !contestProblem.getLevelId().equals("")) {
            gk += contestProblem.getLevelId();
        }
        if (contestProblem.getCategoryId() != null && !contestProblem.getCategoryId().equals("")) {
            gk = gk + "-" + contestProblem.getCategoryId();
        }
        return gk;
    }

    @Override
    public boolean distributeProblemsOfContestToParticipants(String contestId) {
        ProgrammingContest programmingContest = programmingContestRepo.findByContestId(contestId);
        if (programmingContest == null) {
            log.info("distributeProblemsOfContestToParticipants, cannot find contest " + contestId);
            return false;
        }
        if (programmingContest.getContestTypeId().equals(ProgrammingContest.CONTEST_TYPE_PARTICIPANT_IDENTICAL)) {
            log.info("distributeProblemsOfContestToParticipants, this contest is IDENTICAL");
            return false;
        }
        // get list of problems assigned to the contest
        List<ContestProblem> contestProblems = getProblemsOfContest(contestId);

        // get list of approved participants to the contest
        List<ProgrammingContestUserRegistration> programmingContestUserRegistrations = programmingContestUserRegistrationRepo
            .findByContestIdAndStatusId(contestId, ProgrammingContestUserRegistration.REGISTATION_STATUS_APPROVED);

        HashSet<String> groups = new HashSet();
        for (ContestProblem contestProblem : contestProblems) {
            groups.add(getGroupKey(contestProblem));
        }
        HashMap<String, List<ContestProblem>> mapKey2ContestProblem = new HashMap();
        for (ContestProblem contestProblem : contestProblems) {
            String gk = getGroupKey(contestProblem);
            if (mapKey2ContestProblem.get(gk) == null) {
                mapKey2ContestProblem.put(gk, new ArrayList<ContestProblem>());
            }
            mapKey2ContestProblem.get(gk).add(contestProblem);
        }
        for (ProgrammingContestUserRegistration pcur : programmingContestUserRegistrations) {
            String userLoginId = pcur.getUserLoginId();
            for (String gk : groups) {
                List<ContestProblem> L = mapKey2ContestProblem.get(gk);
                // pick randomly a problem from L
                Random R = new Random();
                ContestProblem p = L.get(R.nextInt(L.size()));

                // add problem p with participant userLoginId
                ProgrammingContestUserRegistrationProblem programmingContestUserRegistrationProblem = new ProgrammingContestUserRegistrationProblem();
                programmingContestUserRegistrationProblem.setContestId(contestId);
                programmingContestUserRegistrationProblem.setUserLoginId(userLoginId);
                programmingContestUserRegistrationProblem.setProblemId(p.getProblemId());

                programmingContestUserRegistrationProblem = programmingContestUserRegistrationProblemRepo.save(
                    programmingContestUserRegistrationProblem);

                log.info("distributeProblemsOfContestToParticipants, distribute problem " +
                         p.getProblemId() +
                         " to participant "
                         +
                         userLoginId +
                         " in the contest " +
                         contestId);
            }
        }
        return false;
    }
}
