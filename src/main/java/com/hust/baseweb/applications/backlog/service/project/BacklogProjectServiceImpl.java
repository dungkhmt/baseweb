package com.hust.baseweb.applications.backlog.service.project;

import com.hust.baseweb.applications.backlog.entity.BacklogProject;
import com.hust.baseweb.applications.backlog.entity.BacklogProjectMember;
import com.hust.baseweb.applications.backlog.model.CreateProjectInputModel;
import com.hust.baseweb.applications.backlog.repo.BacklogProjectMemberRepo;
import com.hust.baseweb.applications.backlog.repo.BacklogProjectRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BacklogProjectServiceImpl implements BacklogProjectService {

    @Autowired
    BacklogProjectRepo backlogProjectRepo;
    @Autowired
    BacklogProjectMemberRepo backlogProjectMemberRepo;

    @Override
    public BacklogProject save(CreateProjectInputModel input) {
        BacklogProject backlogProject = backlogProjectRepo.findByBacklogProjectId(input.getBacklogProjectId());
        if (backlogProject == null) {
            return backlogProjectRepo.save(new BacklogProject(input));
        }
        return null;
    }

    @Override
    public List<BacklogProject> findAll() {
        // TODO Auto-generated method stub
        return backlogProjectRepo.findAll();
    }

    @Override
    public BacklogProject findByBacklogProjectId(String backlogProjectId) {
        // TODO Auto-generated method stub
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
        if ((backlogProjectRepo.existsByBacklogProjectId(projectModel.getBacklogProjectId()))) {
            return new BacklogProject();
        }
        return backlogProjectRepo.save(new BacklogProject(
            projectModel.getBacklogProjectId(),
            projectModel.getBacklogProjectName()
        ));
    }
}
