package com.hust.baseweb.applications.backlog.repo;

import com.hust.baseweb.applications.backlog.entity.BacklogProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BacklogProjectRepo extends JpaRepository<BacklogProject, String> {
    BacklogProject save(BacklogProject backlogProject);
    BacklogProject findByBacklogProjectId(UUID backlogProjectId);
    List<BacklogProject> findAllByBacklogProjectCode(String projectCode);
    List<BacklogProject> findAllByBacklogProjectName(String projectName);
    boolean existsByBacklogProjectId(String backlogProjectId);
}
