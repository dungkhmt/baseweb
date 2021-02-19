package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.ProgrammingContestUserRegistration;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateProgrammingContestUserRegistrationInputModel;
import com.hust.baseweb.applications.education.programsubmisson.repo.ProgrammingContestUserRegistrationRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProgrammingContestUserRegistrationServiceImpl implements ProgrammingContestUserRegistrationService {
    private ProgrammingContestUserRegistrationRepo programmingContestUserRegistrationRepo;

    @Override
    public ProgrammingContestUserRegistration save(CreateProgrammingContestUserRegistrationInputModel input) {
        ProgrammingContestUserRegistration programmingContestUserRegistration = new ProgrammingContestUserRegistration();
        programmingContestUserRegistration.setContestId(input.getContestId());
        programmingContestUserRegistration.setUserLoginId(input.getUserLoginId());
        programmingContestUserRegistration = programmingContestUserRegistrationRepo.save(programmingContestUserRegistration);
        return programmingContestUserRegistration;
    }

    @Override
    public List<ProgrammingContestUserRegistration> findAll() {
        return programmingContestUserRegistrationRepo.findAll();
    }
}
