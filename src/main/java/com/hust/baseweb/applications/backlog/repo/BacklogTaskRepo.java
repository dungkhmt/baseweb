package com.hust.baseweb.applications.backlog.repo;

import com.hust.baseweb.applications.backlog.entity.BacklogProject;
import com.hust.baseweb.applications.backlog.entity.BacklogTask;
import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskInputModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BacklogTaskRepo extends JpaRepository<BacklogTask, UUID> {
    BacklogTask save(BacklogTask backlogTask);
    List<BacklogTask> findByBacklogProjectId(String backlogProjectId);
    BacklogTask findByBacklogTaskId(UUID backlogTaskId);
}
