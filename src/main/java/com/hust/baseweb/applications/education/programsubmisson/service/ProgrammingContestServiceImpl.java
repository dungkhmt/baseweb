package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContest;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateProgrammingContestInputModel;
import com.hust.baseweb.applications.education.programsubmisson.repo.ProgrammingContestRepo;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProgrammingContestServiceImpl implements ProgrammingContestService{
    private ProgrammingContestRepo programmingContestRepo;

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
}
