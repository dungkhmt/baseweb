package com.hust.baseweb.applications.backlog.service.task;

import com.hust.baseweb.applications.backlog.entity.BacklogTask;
import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskInputModel;
import com.hust.baseweb.applications.backlog.repo.BacklogTaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BacklogTaskServiceImpl implements BacklogTaskService {

    @Autowired
    BacklogTaskRepo backlogTaskRepo;

    @Override
    public BacklogTaskService save(CreateBacklogTaskInputModel input) {
        return null;
    }

    @Override
    public List<BacklogTask> findByBacklogProjectId(String backlogProjectId) {
        return backlogTaskRepo.findByBacklogProjectId(backlogProjectId);
    }

    @Override
    public BacklogTask findByBacklogTaskId(UUID backlogTaskId) {
        return backlogTaskRepo.findByBacklogTaskId(backlogTaskId);
    }

    @Override
    public BacklogTask create(CreateBacklogTaskInputModel input, String userLoginId) throws ParseException {

//        input.setCreatedStamp(date);
//        input.setCreatedDate(date);
//        input.setLastUpdateStamp(date);
        input.setCreatedByUserLoginId(userLoginId);

        return backlogTaskRepo.save(new BacklogTask(input));
    }

    @Override
    public BacklogTask update(CreateBacklogTaskInputModel input) {
        BacklogTask task = backlogTaskRepo.findByBacklogTaskId(input.getBacklogTaskId());
        Date date = new Date();
        input.setLastUpdateStamp(date);
        task.update(input);

        return backlogTaskRepo.save(task);
    }

    @Override
    public void saveAttachment(MultipartFile file) {

    }
}
