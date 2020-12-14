package com.hust.baseweb.applications.backlog.service.project;

import com.hust.baseweb.applications.backlog.entity.BacklogProject;
import com.hust.baseweb.applications.backlog.model.CreateProjectInputModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BacklogProjectService {
    BacklogProject save(CreateProjectInputModel input);
    List<BacklogProject> findAll();
    BacklogProject findByBacklogProjectId(String backlogProjectId);
    List<BacklogProject> findAllByMemberPartyId(UUID partyId);
    BacklogProject create(CreateProjectInputModel projectModel);
}
