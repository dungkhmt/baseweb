package com.hust.baseweb.applications.backlog.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AssignmentSuggestionSolverInput {

    private UUID backlogTaskId;
    private Date startDate;
    private Date endDate;
    private List<UUID> assignablePartyIds;
}
