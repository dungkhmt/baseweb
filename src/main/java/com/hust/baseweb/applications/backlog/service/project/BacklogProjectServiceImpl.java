package com.hust.baseweb.applications.backlog.service.project;

import com.hust.baseweb.applications.backlog.entity.BacklogProject;
import com.hust.baseweb.applications.backlog.entity.BacklogProjectMember;
import com.hust.baseweb.applications.backlog.model.CreateProjectInputModel;
import com.hust.baseweb.applications.backlog.repo.BacklogProjectMemberRepo;
import com.hust.baseweb.applications.backlog.repo.BacklogProjectRepo;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BacklogProjectServiceImpl implements BacklogProjectService {

    BacklogProjectRepo backlogProjectRepo;
    BacklogProjectMemberRepo backlogProjectMemberRepo;

    @Override
    public BacklogProject save(CreateProjectInputModel input) {
        return backlogProjectRepo.save(new BacklogProject(input));
    }

    @Override
    public List<BacklogProject> findAll() {
        return backlogProjectRepo.findAll();
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
