package com.hust.baseweb.applications.backlog.service.task;

import com.hust.baseweb.applications.backlog.entity.BacklogTask;
import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskInputModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@Service
public interface BacklogTaskService {
    BacklogTaskService save(CreateBacklogTaskInputModel input);
    List<BacklogTask> findByBacklogProjectId(String backlogProjectId);
    BacklogTask findByBacklogTaskId(UUID backlogTaskId);
    BacklogTask create(CreateBacklogTaskInputModel input, String userLoginId) throws ParseException;
    BacklogTask update(CreateBacklogTaskInputModel input);
    void saveAttachment(MultipartFile file);
}
