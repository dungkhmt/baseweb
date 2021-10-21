package com.hust.baseweb.applications.education.programmingcontest.repo;

import com.hust.baseweb.applications.education.programmingcontest.entity.ContestProblemNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface ContestProblemRepoNew extends JpaRepository<ContestProblemNew, String> {
    ContestProblemNew findByProblemId(String problemId);

    @Query("select cp.problemName from ContestProblemNew cp")
    ArrayList<String> findAllProblemName();
}
