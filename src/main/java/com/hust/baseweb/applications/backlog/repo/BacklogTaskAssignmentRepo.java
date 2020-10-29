package com.hust.baseweb.applications.backlog.repo;

import com.hust.baseweb.applications.backlog.entity.BacklogTaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BacklogTaskAssignmentRepo extends JpaRepository<BacklogTaskAssignment, UUID> {
    BacklogTaskAssignment save(BacklogTaskAssignment backlogTaskAssignment);
    List<BacklogTaskAssignment> findAllByBacklogTaskId(UUID backlogTaskId);
    List<BacklogTaskAssignment> findAllByBacklogTaskIdAndStatusIdEquals(UUID backlogTaskId, String statusId);
    BacklogTaskAssignment findByBacklogTaskIdAndAndAssignedToPartyId(UUID backlogTaskId, UUID assignedToPartyId);
}
