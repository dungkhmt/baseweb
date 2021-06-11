package com.hust.baseweb.applications.education.programsubmisson.repo;

import com.hust.baseweb.applications.education.programsubmisson.entity.CompositeProgrammingContestProblemId;
import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContestProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgrammingContestProblemRepo extends JpaRepository<ProgrammingContestProblem,
    CompositeProgrammingContestProblemId> {

    ProgrammingContestProblem save(ProgrammingContestProblem programmingContestProblem);

    List<ProgrammingContestProblem> findByContestId(String contestId);
}
