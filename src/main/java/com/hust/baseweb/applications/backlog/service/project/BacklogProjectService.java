package com.hust.baseweb.applications.backlog.service.project;

import com.hust.baseweb.applications.backlog.entity.BacklogProject;
import com.hust.baseweb.applications.backlog.model.CreateProjectInputModel;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BacklogProjectService {
    BacklogProject save(UserLogin userLogin, CreateProjectInputModel input);
    List<BacklogProject> findAll();
    List<BacklogProject> findByProjectCode(String projectCode);
    List<BacklogProject> findByProjectName(String projectName);
    BacklogProject findByBacklogProjectId(UUID backlogProjectId);
    List<BacklogProject> findAllByMemberPartyId(UUID partyId);
    BacklogProject create(CreateProjectInputModel projectModel);
}
