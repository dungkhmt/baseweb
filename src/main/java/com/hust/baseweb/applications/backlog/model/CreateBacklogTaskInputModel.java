package com.hust.baseweb.applications.backlog.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class CreateBacklogTaskInputModel {
    private UUID backlogTaskId;
    private String backlogTaskName;
    private String backlogTaskCategoryId;
    private String backlogDescription;
    private String backlogProjectId;
    private Date createdDate;
    private String createdByUserLoginId;
    private Date fromDate;
    private Date dueDate;
    private String statusId;
    private String priorityId;
    private Date lastUpdateStamp;
    private Date createdStamp;
    private String[] attachmentPaths;
}
