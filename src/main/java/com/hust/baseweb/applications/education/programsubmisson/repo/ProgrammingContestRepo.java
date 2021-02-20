package com.hust.baseweb.applications.education.programsubmisson.repo;

import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgrammingContestRepo extends JpaRepository<ProgrammingContest, String> {
    ProgrammingContest save(ProgrammingContest programmingContest);
    ProgrammingContest findByContestId(String contestId);
}
