package com.hust.baseweb.applications.backlog.service.task;

import com.hust.baseweb.applications.backlog.entity.BacklogTaskAssignable;
import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskAssignableInputModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BacklogTaskAssignableService {
    List<BacklogTaskAssignable> save(CreateBacklogTaskAssignableInputModel input);
    List<BacklogTaskAssignable> findAllByBacklogTaskId(UUID backlogTaskId);
    List<BacklogTaskAssignable> findAllActiveByBacklogTaskId(UUID backlogTaskId);
}
