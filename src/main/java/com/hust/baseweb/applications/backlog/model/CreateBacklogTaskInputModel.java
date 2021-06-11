package com.hust.baseweb.applications.backlog.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class CreateBacklogTaskInputModel {

    private UUID backlogTaskId;
    private String backlogTaskName;
    private String categoryId;
    private String backlogDescription;
    private UUID backlogProjectId;
    private Date createdDate;
    private String createdByUserLoginId;
    private Date fromDate;
    private Date dueDate;
    private String statusId;
    private String priorityId;
    private Date lastUpdateStamp;
    private Date createdStamp;
    private String[] attachmentPaths;
    private String[] attachmentStatus;
}
