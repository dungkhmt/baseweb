package com.hust.baseweb.applications.backlog.service.task;

import com.hust.baseweb.applications.backlog.entity.BacklogTask;
import com.hust.baseweb.applications.backlog.entity.BacklogTaskAssignable;
import com.hust.baseweb.applications.backlog.entity.BacklogTaskAssignment;
import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskAssignableInputModel;
import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskAssignmentInputModel;
import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskInputModel;
import com.hust.baseweb.applications.backlog.model.ProjectFilterParamsModel;
import com.hust.baseweb.applications.backlog.repo.BacklogTaskRepo;
import com.hust.baseweb.applications.backlog.service.Storage.BacklogFileStorageServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BacklogTaskServiceImpl implements BacklogTaskService {

    private BacklogTaskRepo backlogTaskRepo;
    private BacklogFileStorageServiceImpl storageService;
    private BacklogTaskAssignmentService backlogTaskAssignmentService;
    private BacklogTaskAssignableService backlogTaskAssignableService;

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
    public Page<BacklogTask> findByBacklogProjectId(UUID backlogProjectId, Pageable pageable, ProjectFilterParamsModel filter) {
        Page<BacklogTask> taskList = backlogTaskRepo.findByBacklogProjectId(backlogProjectId, pageable, filter);
        if(taskList == null) return new PageImpl<>(new ArrayList<>(), pageable, 0);
        return taskList;
    }

    @Override
    public Page<BacklogTask> findByBacklogProjectIdAndPartyAssigned(UUID backlogProjectId, UUID assignedPartyId, ProjectFilterParamsModel filter, Pageable pageable) {
        Page<BacklogTask> taskList = backlogTaskRepo.findByBacklogProjectIdAndPartyAssigned(backlogProjectId, assignedPartyId, filter, pageable);
        if(taskList == null) return new PageImpl<>(new ArrayList<>(), pageable, 0);
        return taskList;
    }

    @Override
    public Page<BacklogTask> findOpeningTaskByCreatedUserLogin(
        UUID backlogProjectId,
        String userLoginId,
        ProjectFilterParamsModel filter,
        Pageable pageable
    ) {
        Page<BacklogTask> taskList = backlogTaskRepo.findOpeningTaskByCreatedUserLogin(backlogProjectId, userLoginId, filter, pageable);
        if(taskList == null) return new PageImpl<>(new ArrayList<>(), pageable, 0);
        return taskList;
    }

    @Override
    public BacklogTask findByBacklogTaskId(UUID backlogTaskId) {
        BacklogTask task = backlogTaskRepo.findByBacklogTaskId(backlogTaskId);
        if(task == null) return new BacklogTask();
        return task;
    }

    @Override
    @Transactional
    public BacklogTask create(
        CreateBacklogTaskInputModel taskInput,
        CreateBacklogTaskAssignmentInputModel assignmentInput,
        CreateBacklogTaskAssignableInputModel assignableInput,
        MultipartFile[] files,
        String userLoginId
    ) {
        taskInput.setCreatedByUserLoginId(userLoginId);

        // save attachment files
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String prefixFileName = formatter.format(now);
        ArrayList<String> attachmentPaths = new ArrayList<>();
        Arrays.asList(files).forEach(file -> {
            try {
                storageService.store(file, "", prefixFileName + "-" + file.getOriginalFilename());
                attachmentPaths.add(prefixFileName + "-" + file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        taskInput.setAttachmentPaths(attachmentPaths.toArray(new String[0]));
        //
        BacklogTask task = backlogTaskRepo.save(new BacklogTask(taskInput));

        // create assignment, assignable
        assignmentInput.setBacklogTaskId(task.getBacklogTaskId());
        assignableInput.setBacklogTaskId(task.getBacklogTaskId());

        backlogTaskAssignmentService.create(assignmentInput);
        backlogTaskAssignableService.save(assignableInput);

        return task;
    }

    @Override
    @Transactional
    public BacklogTask update(
        CreateBacklogTaskInputModel taskInput,
        CreateBacklogTaskAssignmentInputModel assignmentInput,
        CreateBacklogTaskAssignableInputModel assignableInput,
        MultipartFile[] files,
        String userLoginId
    ) throws IOException {
        BacklogTask task = backlogTaskRepo.findByBacklogTaskId(taskInput.getBacklogTaskId());
        if(task == null) return new BacklogTask();
        if(!task.getCreatedByUserLoginId().equals(userLoginId)) return new BacklogTask();

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String prefixFileName = formatter.format(now);

        String[] savedFileNames = task.getAttachmentPaths().split(";");
        ArrayList<String> newAttachmentPaths = new ArrayList<>();

        for(int i = 0; i < taskInput.getAttachmentPaths().length; i++) {
            boolean isFileExisted = Arrays.asList(savedFileNames).contains(taskInput.getAttachmentPaths()[i]);

            switch(taskInput.getAttachmentStatus()[i]) {
                case "deleted":
                    if(isFileExisted) {
                        storageService.deleteIfExists("", taskInput.getAttachmentPaths()[i]);
                    }
                    break;
                case "new":
                    MultipartFile file = null;
                    for (MultipartFile multipartFile : files) {
                        if (Objects.equals(multipartFile.getOriginalFilename(), taskInput.getAttachmentPaths()[i])) {
                            file = multipartFile;
                            break;
                        }
                    }
                    assert file != null;
                    storageService.store(file, "", prefixFileName + "-" + file.getOriginalFilename());
                    newAttachmentPaths.add(prefixFileName + "-" + taskInput.getAttachmentPaths()[i]);
                    break;
                case "uploaded":
                    newAttachmentPaths.add(taskInput.getAttachmentPaths()[i]);
                    break;
                default:
                    break;
            }
        }
        taskInput.setAttachmentPaths(newAttachmentPaths.toArray(new String[0]));
        task.update(taskInput);
        backlogTaskRepo.save(task);
        assignmentInput.setBacklogTaskId(task.getBacklogTaskId());
        assignableInput.setBacklogTaskId(task.getBacklogTaskId());
        backlogTaskAssignmentService.create(assignmentInput);
        backlogTaskAssignableService.save(assignableInput);

        return task;
    }

    @Override
    public BacklogTask updateTaskStatus(UUID taskId, String newStatus, List<BacklogTaskAssignment> assignments) {
        BacklogTask task = backlogTaskRepo.findByBacklogTaskId(taskId);

        if(!newStatus.equals(task.getStatusId())) {
            for (BacklogTaskAssignment assignment : assignments) {
                // change compare value if import other status in database
                switch (newStatus) {
                    case "TASK_RESOLVED":
                    case "TASK_CLOSED":
                        Date finishedDate = new Date();
                        assignment.setFinishedDate(finishedDate);
                        backlogTaskAssignmentService.save(assignment);
                        break;
                    case "TASK_OPEN":
                        break;
                    case "TASK_INPROGRESS":
                        Date startDate = new Date();
                        assignment.setStartDate(startDate);
                        backlogTaskAssignmentService.save(assignment);
                        break;
                }
            }
        }
        task.setStatusId(newStatus);
        BacklogTask updatedTask = backlogTaskRepo.save(task);
        return updatedTask;
    }

}
