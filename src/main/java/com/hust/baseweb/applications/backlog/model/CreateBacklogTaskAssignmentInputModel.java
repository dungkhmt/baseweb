package com.hust.baseweb.applications.backlog.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateBacklogTaskAssignmentInputModel {

    private UUID backlogTaskId;
    private List<UUID> assignedToPartyId;
    private Date startDate;
    private Date finishedDate;
    private String statusId;
}
