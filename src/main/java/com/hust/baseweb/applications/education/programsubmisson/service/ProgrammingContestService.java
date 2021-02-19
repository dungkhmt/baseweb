package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContest;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateProgrammingContestInputModel;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProgrammingContestService {
    ProgrammingContest save(UserLogin userLogin, CreateProgrammingContestInputModel input);
    List<ProgrammingContest> findAll();
}
