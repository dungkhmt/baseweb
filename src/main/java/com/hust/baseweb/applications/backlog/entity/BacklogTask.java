package com.hust.baseweb.applications.backlog.entity;

import com.hust.baseweb.applications.backlog.model.CreateBacklogTaskInputModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
        backlogTaskCategoryId = input.getBacklogTaskCategoryId();
        backlogDescription = input.getBacklogDescription();
        backlogProjectId = input.getBacklogProjectId();
        createdDate = input.getCreatedDate();
        createdByUserLoginId = input.getCreatedByUserLoginId();
        fromDate = input.getFromDate();
        dueDate = input.getDueDate();
        statusId = input.getStatusId();
        priorityId = input.getPriorityId();
        lastUpdateStamp = input.getLastUpdateStamp();
        createdStamp = input.getCreatedStamp();
    }

    @Id
    @Column(name = "backlog_task_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID backlogTaskId;

    @Column(name = "backlog_task_name")
    private String backlogTaskName;

    @Column(name = "backlog_task_category_id")
    private String backlogTaskCategoryId;

    @Column(name = "backlog_description")
    private String backlogDescription;

    @Column(name = "backlog_project_id")
    private String backlogProjectId;

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

    @Column(name = "created_stamp")
    private Date createdStamp;

    public void update(CreateBacklogTaskInputModel input) {
        if(input.getBacklogTaskName() != null) {
            backlogTaskName = input.getBacklogTaskName();
        }

        if(input.getBacklogTaskCategoryId() != null) {
            backlogTaskCategoryId = input.getBacklogTaskCategoryId();
        }

        if(input.getBacklogDescription() != null) {
            backlogDescription = input.getBacklogDescription();
        }

        if(input.getDueDate() != null) {
            dueDate = input.getDueDate();
        }

        if(input.getStatusId() != null) {
            statusId = input.getStatusId();
        }

        if(input.getPriorityId() != null) {
            priorityId = input.getPriorityId();
        }

        if(input.getLastUpdateStamp() != null) {
            lastUpdateStamp = input.getLastUpdateStamp();
        }
    }
}
