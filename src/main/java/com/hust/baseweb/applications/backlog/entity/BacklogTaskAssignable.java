package com.hust.baseweb.applications.backlog.entity;

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
@Table(name = "backlog_task_assignable")
public class BacklogTaskAssignable {

    public BacklogTaskAssignable(
        UUID backlogTaskId,
        UUID assignedToPartyId,
        String statusId,
        Date startDate,
        Date finishedDate
    ) {
        this.backlogTaskId = backlogTaskId;
        this.assignedToPartyId = assignedToPartyId;
        this.startDate = startDate;
        this.finishedDate = finishedDate;
        this.statusId = statusId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "backlog_task_assignable_id")
    private UUID backlogTaskAssignmentId;

    @Column(name = "backlog_task_id")
    private UUID backlogTaskId;

    @Column(name = "assigned_to_party_id")
    private UUID assignedToPartyId;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "finished_date")
    private Date finishedDate;

    @Column(name = "status_id")
    private String statusId;

    @Column(name = "last_updated_stamp")
    private Date lastUpdatedStamp;

    @Column(name = "created_stamp", insertable = false, updatable = false)
    private Date createdStamp;
}
