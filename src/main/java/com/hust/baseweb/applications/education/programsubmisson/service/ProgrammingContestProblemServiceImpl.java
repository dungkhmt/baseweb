package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContestProblem;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateProgrammingContestProblemInputModel;
import com.hust.baseweb.applications.education.programsubmisson.repo.ProgrammingContestProblemRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class ProgrammingContestProblemServiceImpl implements ProgrammingContestProblemService {

    private ProgrammingContestProblemRepo programmingContestProblemRepo;

    @Transactional
    @Override
    public ProgrammingContestProblem save(CreateProgrammingContestProblemInputModel input) {
        ProgrammingContestProblem programmingContestProblem = new ProgrammingContestProblem();
        programmingContestProblem.setContestId(input.getContestId());
        programmingContestProblem.setProblemId(input.getProblemId());
        programmingContestProblem = programmingContestProblemRepo.save(programmingContestProblem);


        return programmingContestProblem;
    }

    @Override
    public List<ProgrammingContestProblem> findAll() {
        List<ProgrammingContestProblem> programmingContestProblems = programmingContestProblemRepo.findAll();
        return programmingContestProblems;
    }

    @Override
    public List<ProgrammingContestProblem> findByContestId(String contestId) {
        return programmingContestProblemRepo.findByContestId(contestId);
    }
}
