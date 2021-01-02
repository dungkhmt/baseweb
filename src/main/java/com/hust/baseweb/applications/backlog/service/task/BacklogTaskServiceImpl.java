package com.hust.baseweb.applications.backlog.service.task;

import com.hust.baseweb.applications.backlog.entity.BacklogTask;
import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskInputModel;
import com.hust.baseweb.applications.backlog.repo.BacklogTaskRepo;
import com.hust.baseweb.applications.backlog.service.Storage.BacklogFileStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BacklogTaskServiceImpl implements BacklogTaskService {

    @Autowired
    BacklogTaskRepo backlogTaskRepo;
    @Autowired
    private BacklogFileStorageServiceImpl storageService;

    @Override
    public BacklogTask save(BacklogTask task) {
        return backlogTaskRepo.save(task);
    }

    @Override
    public List<BacklogTask> findByBacklogProjectId(UUID backlogProjectId) {
        List<BacklogTask> taskList = backlogTaskRepo.findByBacklogProjectId(backlogProjectId);
        if(taskList == null) return  new ArrayList<>();
        return taskList;
    }

    @Override
    public BacklogTask findByBacklogTaskId(UUID backlogTaskId) {
        BacklogTask task = backlogTaskRepo.findByBacklogTaskId(backlogTaskId);
        if(task == null) return new BacklogTask();
        return task;
    }

    @Override
    public BacklogTask create(CreateBacklogTaskInputModel input, String userLoginId) {

//        input.setCreatedStamp(date);
//        input.setCreatedDate(date);
//        input.setLastUpdateStamp(date);
        input.setCreatedByUserLoginId(userLoginId);

        return backlogTaskRepo.save(new BacklogTask(input));
    }

    @Override
    public BacklogTask update(CreateBacklogTaskInputModel input) throws IOException {
        BacklogTask task = backlogTaskRepo.findByBacklogTaskId(input.getBacklogTaskId());
        Date date = new Date();
        input.setLastUpdateStamp(date);
        ArrayList<String> deleteFiles = task.update(input);
        for(String file: deleteFiles) {
            storageService.deleteIfExists("", file);
        }

        return backlogTaskRepo.save(task);
    }

    @Override
    public void saveAttachment(MultipartFile file) {

    }
}
