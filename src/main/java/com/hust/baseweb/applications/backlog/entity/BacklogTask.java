package com.hust.baseweb.applications.backlog.entity;

import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskInputModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "backlog_task")
public class BacklogTask {

    public BacklogTask(CreateBacklogTaskInputModel input) {
        backlogTaskName = input.getBacklogTaskName();
        categoryId = input.getCategoryId();
        backlogDescription = input.getBacklogDescription();
        backlogProjectId = input.getBacklogProjectId();
        createdByUserLoginId = input.getCreatedByUserLoginId();
        fromDate = input.getFromDate();
        dueDate = input.getDueDate();
        statusId = input.getStatusId();
        priorityId = input.getPriorityId();
//        createdDate = input.getCreatedDate();
//        lastUpdateStamp = input.getLastUpdateStamp();
//        createdStamp = input.getCreatedStamp();

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String prefixFileName = formatter.format(now);
        StringBuilder attachmentPaths = new StringBuilder();

        for(int i = 0; i < input.getAttachmentPaths().length; i++) {
            attachmentPaths.append(prefixFileName).append("-").append(input.getAttachmentPaths()[i]);
            if(i < input.getAttachmentPaths().length - 1) {
                attachmentPaths.append(";");
            }
        }
        this.attachmentPaths = attachmentPaths.toString();
    }

    @Id
    @Column(name = "backlog_task_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID backlogTaskId;

    @Column(name = "backlog_task_name")
    private String backlogTaskName;

    @Column(name = "backlog_task_category_id")
    private String categoryId;

    @Column(name = "backlog_description")
    private String backlogDescription;

    @Column(name = "backlog_project_id")
    private UUID backlogProjectId;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "created_by_user_login_id")
    private String createdByUserLoginId;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "status_id")
    private String statusId;

    @Column(name = "priority_id")
    private String priorityId;

    @Column(name = "last_updated_stamp")
    private Date lastUpdateStamp;

    @Column(name = "created_stamp", insertable = false, updatable = false)
    private Date createdStamp;

    @Column(name = "attachment_paths")
    private String attachmentPaths;

    public ArrayList<String> update(CreateBacklogTaskInputModel input) {
        if(input.getBacklogTaskName() != null) backlogTaskName = input.getBacklogTaskName();
        if(input.getCategoryId() != null) categoryId = input.getCategoryId();
        if(input.getBacklogDescription() != null) backlogDescription = input.getBacklogDescription();
        if(input.getDueDate() != null) dueDate = input.getDueDate();
        if(input.getStatusId() != null) statusId = input.getStatusId();
        if(input.getPriorityId() != null) priorityId = input.getPriorityId();
        if(input.getLastUpdateStamp() != null) lastUpdateStamp = input.getLastUpdateStamp();

        // update attachmentPaths
        String[] savedNames = this.attachmentPaths.split(";");
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String prefixFileName = formatter.format(now);
        StringBuilder newAttachmentPaths = new StringBuilder();
        ArrayList<String> needToDelete = new ArrayList<>();

        for(int i = 0; i < input.getAttachmentPaths().length; i++) {
            boolean isFileExisted = Arrays.asList(savedNames).contains(input.getAttachmentPaths()[i]);

            switch(input.getAttachmentStatus()[i]) {
                case "deleted":
                    if(isFileExisted) {
                        needToDelete.add(input.getAttachmentPaths()[i]);
                    }
                    break;
                case "new":
                    newAttachmentPaths.append(prefixFileName).append("-").append(input.getAttachmentPaths()[i]);
                    newAttachmentPaths.append(";");
                    break;
                case "uploaded":
                    newAttachmentPaths.append(input.getAttachmentPaths()[i]);
                    newAttachmentPaths.append(";");
                    break;
                default:
                    break;
            }
        }
        if(newAttachmentPaths.length() > 0) {
            if(newAttachmentPaths.charAt(0) == ';') {
                newAttachmentPaths.deleteCharAt(0);
            }
            if(newAttachmentPaths.length() > 0 && newAttachmentPaths.charAt(newAttachmentPaths.length() - 1) == ';') {
                newAttachmentPaths.deleteCharAt(newAttachmentPaths.length() - 1);
            }
        }
        this.attachmentPaths = newAttachmentPaths.toString();

        return needToDelete;
    }
}
