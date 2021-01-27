package com.hust.baseweb.applications.backlog.service.task;

import com.hust.baseweb.applications.backlog.entity.BacklogTask;
import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskInputModel;
import com.hust.baseweb.applications.backlog.model.ProjectFilterParamsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public interface BacklogTaskService {
    BacklogTask save(BacklogTask task);
    List<BacklogTask> findByBacklogProjectId(UUID backlogProjectId);
    Page<BacklogTask> findByBacklogProjectId(UUID backlogProjectId, Pageable pageable, ProjectFilterParamsModel filter);
    Page<BacklogTask> findByBacklogProjectIdAndPartyAssigned(UUID backlogProjectId, UUID assignedPartyId, ProjectFilterParamsModel filter, Pageable pageable);
    Page<BacklogTask> findOpeningTaskByCreatedUserLogin(UUID backlogProjectId, String userLoginId, ProjectFilterParamsModel filter, Pageable pageable);
    BacklogTask findByBacklogTaskId(UUID backlogTaskId);
    BacklogTask create(CreateBacklogTaskInputModel input, String userLoginId);
    BacklogTask update(CreateBacklogTaskInputModel input) throws IOException;
    void saveAttachment(MultipartFile file);
}
