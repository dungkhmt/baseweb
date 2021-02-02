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
        attachmentPaths = String.join(";", input.getAttachmentPaths());
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

    public BacklogTask update(CreateBacklogTaskInputModel input) {
        if(input.getBacklogTaskName() != null) backlogTaskName = input.getBacklogTaskName();
        if(input.getCategoryId() != null) categoryId = input.getCategoryId();
        if(input.getBacklogDescription() != null) backlogDescription = input.getBacklogDescription();
        if(input.getDueDate() != null) dueDate = input.getDueDate();
        if(input.getFromDate() != null) fromDate = input.getFromDate();
        if(input.getStatusId() != null) statusId = input.getStatusId();
        if(input.getPriorityId() != null) priorityId = input.getPriorityId();
        if(input.getLastUpdateStamp() != null) lastUpdateStamp = input.getLastUpdateStamp();
        if(input.getAttachmentPaths() != null) attachmentPaths = String.join(";", input.getAttachmentPaths());

        return this;
    }
}
