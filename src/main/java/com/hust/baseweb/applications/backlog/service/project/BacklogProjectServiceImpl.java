package com.hust.baseweb.applications.backlog.service.project;

import com.hust.baseweb.applications.backlog.entity.BacklogProject;
import com.hust.baseweb.applications.backlog.entity.BacklogProjectMember;
import com.hust.baseweb.applications.backlog.model.CreateBacklogProjectMemberModel;
import com.hust.baseweb.applications.backlog.model.CreateProjectInputModel;
import com.hust.baseweb.applications.backlog.repo.BacklogProjectMemberRepo;
import com.hust.baseweb.applications.backlog.repo.BacklogProjectRepo;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BacklogProjectServiceImpl implements BacklogProjectService {

    BacklogProjectRepo backlogProjectRepo;
    BacklogProjectMemberRepo backlogProjectMemberRepo;
    BacklogProjectMemberService backlogProjectMemberService;
    @Transactional
    @Override
    public BacklogProject save(UserLogin userLogin, CreateProjectInputModel input) {
        BacklogProject backlogProject  = backlogProjectRepo.save(new BacklogProject(input));
        //if(backlogProject == null) return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        log.info("save, SUCCESS save with projectId = " + backlogProject.getBacklogProjectId());

        UUID userPartyId = userLogin.getParty().getPartyId();
        CreateBacklogProjectMemberModel backlogProjectMemberInput = new CreateBacklogProjectMemberModel(
            backlogProject.getBacklogProjectId(),
            userPartyId
        );
        backlogProjectMemberService.save(backlogProjectMemberInput);

        log.info("save, projectId = " + backlogProject.getBacklogProjectId());
        return backlogProject;
    }

    @Override
    public List<BacklogProject> findAll() {
        return backlogProjectRepo.findAll();
    }

    @Override
    public List<BacklogProject> findByProjectCode(String projectCode) {
        return backlogProjectRepo.findAllByBacklogProjectCode(projectCode);
    }

    @Override
    public List<BacklogProject> findByProjectName(String projectName) {
        return backlogProjectRepo.findAllByBacklogProjectName(projectName);
    }

    @Override
    public BacklogProject findByBacklogProjectId(UUID backlogProjectId) {
        return backlogProjectRepo.findByBacklogProjectId(backlogProjectId);
    }

    @Override
    public List<BacklogProject> findAllByMemberPartyId(UUID partyId) {
        List <BacklogProjectMember> backlogProjectMembers = backlogProjectMemberRepo.findAllByMemberPartyId(partyId);
        List <BacklogProject> projectsByMemberPartyId = new ArrayList<>();
        for(BacklogProjectMember backlogProjectMember: backlogProjectMembers) {
            projectsByMemberPartyId.add(backlogProjectRepo.findByBacklogProjectId(backlogProjectMember.getBacklogProjectId()));
        }
        return projectsByMemberPartyId;
    }

    @Override
    public BacklogProject create(CreateProjectInputModel projectModel) {
        return backlogProjectRepo.save(new BacklogProject(
            new CreateProjectInputModel(
                projectModel.getBacklogProjectCode(),
                projectModel.getBacklogProjectName()
            )
        ));
    }
}
