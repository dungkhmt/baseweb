package com.hust.baseweb.applications.backlog.repo;
import com.hust.baseweb.applications.backlog.entity.BacklogTaskAssignable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BacklogTaskAssignableRepo extends JpaRepository<BacklogTaskAssignable, UUID> {
    BacklogTaskAssignable save(BacklogTaskAssignable backlogTaskAssignable);
    List<BacklogTaskAssignable> findAllByBacklogTaskId(UUID backlogTaskId);
    List<BacklogTaskAssignable> findAllByBacklogTaskIdAndStatusIdEquals(UUID backlogTaskId, String statusId);
    BacklogTaskAssignable findByBacklogTaskIdAndAndAssignedToPartyId(UUID backlogTaskId, UUID assignedToPartyId);
}
